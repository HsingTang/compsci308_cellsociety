package CellSociety;

import CellSociety.Exceptions.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


/**
 * @author Hsingchih Tang
 * Trunk of the cell society project. Control the flow (timeline) of simulation by invoking and
 * connecting up Configurator, XMLAlert, XMLException, XMLParser, IntroScene, UI and Cell classes;
 * Retrieve simulation configuration parameters from Configurator and pass arguments into UI and Cells;
 * Respond to user action of playing/resuming/stepping/switching simulation.
 *
 *
 * Code Refactoring:
 * I split the original Simulation class into two by introducing a new class, Configurator.
 * In comparison to Simulation, which has to somehow work with javaFX in order to control the timeline,
 * Configurator contains purely back-end stuff and is thus the core of the whole program, whereas Simulation
 * still implements some javaFX effects in order to work with purely front-end classes such as IntroScene and UI.
 *
 * Additionally, Simulation is also responsible for setting up text content for alert messages that will be displayed
 * to users when Exceptions occur. The alert messages are stored in a source file; Simulation reads in the text, and
 * creates different SimulationAlert objects for displaying the messages in Alert dialogue boxes. Any Exception that
 * occurs at Configurator and XMLParser when parsing the XML files will eventually be thrown out to Simulation, who
 * handles the Exception by popping up corresponding Alert dialogue to user.
 */
public class Simulation extends Application {


    static final String ALERT_CONFIG_PATH = "resources/XMLAlertText.txt";
    private String myTitle;
    private String SIM_TYPE;
    private double minDelay;
    private double maxDelay;
    private double delay;
    private int myWidth;
    private int myHeight;
    private Timeline myTimeline;
    private Stage myStage;
    private Cell[][] myGrid;
    private Configurator myConfig;
    private Scene myIntroScene;
    private UI myUIScene;


    // XMLAlerts to pop up if encounter exceptions when parsing XML configuration files
    SimulationAlert XMLFileNotFoundAlert = new SimulationAlert();
    SimulationAlert parserConfigAlert = new SimulationAlert();
    SimulationAlert SAXAlert = new SimulationAlert();
    SimulationAlert gridErrAlert = new SimulationAlert();
    SimulationAlert modelErrAlert = new SimulationAlert();
    SimulationAlert paramErrAlert = new SimulationAlert();
    SimulationAlert configErrAlert = new SimulationAlert();
    SimulationAlert neighborErrAlert = new SimulationAlert();
    SimulationAlert stateErrAlert = new SimulationAlert();
    SimulationAlert cellIdxAlert = new SimulationAlert();
    SimulationAlert cellStateAlert = new SimulationAlert();
    SimulationAlert cellConfigAlert = new SimulationAlert();
    SimulationAlert cellInfoAlert = new SimulationAlert();
    private SimulationAlert[] myAlertArr = new SimulationAlert[]{XMLFileNotFoundAlert, parserConfigAlert, SAXAlert, gridErrAlert,
            modelErrAlert, paramErrAlert, configErrAlert, neighborErrAlert, stateErrAlert, cellIdxAlert,
            cellStateAlert, cellConfigAlert, cellInfoAlert};



    /**
     * Constructor of a Simulation object
     * Create a Configurator to read in the default simulation configuration parameters
     * Retrieve parameters from Configurator and set up the simulation
     */
    public Simulation() throws IOException{
        super();
        try{
            myConfig = new Configurator();
        }catch (FileNotFoundException e){
            throw new IOException("Necessary simulation configuration file is missing.",e);
        }
        try {
            setupAlert();
        } catch (FileNotFoundException e) {
            throw new IOException("Necessary alert setup file is missing.", e);
        }
        this.minDelay = myConfig.getMinDelay();
        this.maxDelay = myConfig.getMaxDelay();
        this.myWidth = myConfig.getWidth();
        this.myHeight = myConfig.getHeight();
        this.myTitle = myConfig.getTitle();
        this.myGrid = myConfig.getGrid();
        resetDelay();
    }


    /**
     * Entry point of the program
     * @param stage where scene shall be displayed
     */
    public void start(Stage stage) {
        this.myStage = stage;
        initIntroScene();
    }


    /**
     * @return the 2D array of Cells for this simulation
     * Want both UI and Simulation to both have direct access to the cell objects,
     * so this method returns the exact original 2D cell array stored in Simulation.
     */
    public Cell[][] getGrid() {
        return this.myGrid;
    }


    /**
     * @return a map associating state and corresponding image visualization
     * The original map is stored in Configurator, who hands out the map as an immutable object.
     */
    public Map<String, String> getStateImageMap(){
        return myConfig.getStateImageMap();
    }


    /**
     * @return the current simulation model's XML file path
     */
    public String getSimulationType() {
        return this.SIM_TYPE;
    }


    /**
     * External classes can call this method to change the simulation type, which affects the program choice of
     * which file to read. Simulation will change both its own SIM_TYPE variable and the Configurator's SIM_TYPE.
     * The corresponding XML file should be available in resources folder. If not, a FileNotFoundException will be
     * thrown by XMLParser, received and rethrown by Configurator, and eventually handled by Simulation who pops up
     * an alert dialogue box to user.
     * @param s name of the new simulation's XML file
     */
    public void setSimType(String s) {
        this.SIM_TYPE = s;
        this.myConfig.setConfigSimType(s);
    }


    /**
     * Modify the simulation speed by adjusting delay time between frames based on the passed-in double
     * The double passed in is expected to be between 0 and 1, which indicates the desired delay
     * with respect to default minDelay and maxDelay
     */
    public void setSpeed(Double d) {
        Animation.Status prevStatus = this.myTimeline.getStatus();
        this.myTimeline.stop();
        this.delay = maxDelay-d*(maxDelay-minDelay);
        if(prevStatus== Animation.Status.RUNNING){
            initTimeline();
            playSimulation();
        }else{
            initTimeline();
        }
    }


    /**
     * Public method for starting a new simulation.
     * Reset all simulation model-specific parameters and initialize a new UI and animation timeline.
     * Expected to be called from IntroScene after user has selected a simulation model.
     */
    public void startSimulation(){
        initSimulation();
        initUI();
        initTimeline();
    }


    /**
     * Pause the simulation by pausing the animation timeline.
     * Expected to be called by UI when a pause button is pressed.
     */
    public void pauseSimulation() {
        this.myTimeline.stop();
    }


    /**
     * Run the simulation by resuming/starting the animation timeline.
     * Expected to be called by UI when a start/resume button is pressed.
     */
    public void playSimulation() {
        this.myTimeline.play();
    }


    /**
     * Update Cell states to the next generation and then pause.
     * Expected to be called by UI when a step button is pressed.
     */
    public void stepSimulation() {
        this.myTimeline.pause();
        updateGrid();
    }


    /**
     * Reset all cell states according to XML file's configuration
     * and still remain in the same simulation model
     */
    public void resetSimulation(){
        this.myTimeline.pause();
        resetDelay();
        initSimulation();
        initUI();
        initTimeline();
    }


    /**
     * Switch to the other simulation model by reading the corresponding XML file and reinitializing the grid
     * Expected to be called by UI after User has selected a Simulation type from dropdown menu
     * @param newSimType path to the XML file for the new Simulation
     */
    public void switchSimulation(String newSimType){
        this.myTimeline.stop();
        this.setSimType(newSimType);
        initSimulation();
        initUI();
        resetDelay();
        initTimeline();
    }


    /**
     * Set up error messages of SimulationAlert
     * Alert dialogue boxes will pop up if an XML configuration file is mal-formatted
     *
     * @throws FileNotFoundException if the source file storing alert messages is not found
     */
    private void setupAlert() throws FileNotFoundException {
        Scanner sc;
        try{
            sc=new Scanner(new File(ALERT_CONFIG_PATH));
        } catch (FileNotFoundException e){
            throw e;
        }
        int idx = 0;
        while (sc.hasNextLine() && idx < myAlertArr.length) {
            String[] alertText = sc.nextLine().split(";");
            myAlertArr[idx].setText(alertText[0], alertText[1], alertText[2]);
            idx++;
        }
    }


    /**
     * Initialize the introduction scene where user can choose type of simulation to run
     * Set stage to the initialized IntroScene
     */
    private void initIntroScene() {
        Group myIntroRoot = new Group();
        myIntroScene = new IntroScene(myIntroRoot, myWidth, myHeight, this);
        myStage.setScene(myIntroScene);
        myStage.setTitle(myTitle);
        myStage.show();
    }


    /**
     * Initialize the UI class for creating visualization of the simulation.
     */
    private void initUI() {
        Group myUIRoot = new Group();
        myUIScene = new UI(myUIRoot, myWidth, myHeight, myConfig.getCellShape(), myConfig.getParamList(), this);
        myUIScene.drawGrid();
        myUIScene.drawGraph();
        myStage.setScene(myUIScene);
        myStage.show();
    }


    /**
     * Initialize a new timeline for the simulation.
     * updateGrid() is called by eventHandler of the frame each 'delay' interval.
     */
    private void initTimeline() {
        var frame = new KeyFrame(Duration.millis(delay), e -> updateGrid());
        this.myTimeline = new Timeline();
        this.myTimeline.setCycleCount(Timeline.INDEFINITE);
        this.myTimeline.getKeyFrames().add(frame);
    }


    /**
     * Call the Configurator to read in XML file and initialize the grid of cells.
     * Any Exception that occurs at XMLParser or Configurator will be thrown out and
     * get handled here by popping up a corresponding alert dialogue box.
     */
    private void initSimulation(){
        try{
            myGrid = myConfig.initGrid();
        }catch (CellIdxException e){
            cellIdxAlert.showAlert();
        }catch (CellInfoException e){
            cellInfoAlert.showAlert();
        }catch (CellSpecException e){
            cellConfigAlert.showAlert();
        }catch (SpecErrException e){
            configErrAlert.showAlert();
        }catch (GridErrException e){
            gridErrAlert.showAlert();
        }catch (ModelErrException e){
            modelErrAlert.showAlert();
        }catch (NeighborErrException e){
            neighborErrAlert.showAlert();
        }catch (ParamErrException e){
            paramErrAlert.showAlert();
        }catch (StateErrException e){
            stateErrAlert.showAlert();
        }catch (SAXException e){
            SAXAlert.showAlert();
        }catch (IOException e){
            XMLFileNotFoundAlert.showAlert();
        }catch (ParserConfigurationException e){
            parserConfigAlert.showAlert();
        }
    }


    /**
     * Set the delay for the animation frame to the default value,
     * which is the average of default minDelay and maxDelay.
     */
    private void resetDelay(){
        this.delay = minDelay/2+maxDelay/2;
    }


    /**
     * Update all Cells' states in the grid and notify UI to update visualization.
     */
    private void updateGrid() {
        for (Cell[] row : myGrid) {
            for (Cell currCell:row) {
                currCell.findNextState();
            }
        }
        for (Cell[] row : myGrid) {
            for (Cell currCell:row) {
                currCell.updateState();
            }
        }
        this.myUIScene.drawGrid();
        this.myUIScene.drawGraph();
    }


    /**
     * Main method to launch the Breakout game program.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
