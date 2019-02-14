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
 * Trunk of the cell society project
 * Control the simulation flow by invoking and connecting XMLParser, IntroScene, UI and Cell classes
 * Retrieve simulation configuration parameters from XMLParser and pass arguments into UI and Cells
 * Respond to user action of playing/resuming/stepping/switching simulation
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


    // XMLAlerts to pop up when encountering mal-formatted XML file
    // package-private variables
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
     * Call readConfig() to set up the Simulation class with specific parameters
     */
    public Simulation() throws IOException{
        super();
        try{
            myConfig = new Configurator();
        }catch (FileNotFoundException e){
            throw new IOException("Simulation configuration file is missing.",e);
        }
        try {
            setupAlert();
        } catch (FileNotFoundException e) {
            throw new IOException("Alert Setup file is missing.", e);
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
     * Set up error messages of SimulationAlert
     * Alert dialogue boxes will pop up if an XML configuration file is mal-formatted
     *
     * @throws FileNotFoundException if the source file storing error message text is not found
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
     * External classes can call this method to change the simulation type
     * The corresponding XML file should be available in resources folder
     * @param s name of the new simulation's XML file
     */
    public void setSimType(String s) {
        this.SIM_TYPE = s;
        this.myConfig.setConfigSimType(s);
    }


    private void resetDelay(){
        this.delay = minDelay/2+maxDelay/2;
    }

    /**
     * Initialize the UI class for creating visualization of the simulation
     */
    private void initUI() {
        Group myUIRoot = new Group();
        //myUIScene = new UI(myUIRoot, myWidth, myHeight, this);
        myUIScene = new UI(myUIRoot, myWidth, myHeight, myConfig.getCellShape(), myConfig.getParamList(), this);
        myUIScene.drawGrid();
        myUIScene.drawGraph();
        myStage.setScene(myUIScene);
        myStage.show();
    }


    /**
     * Initialize a new timeline for the simulation
     * updateGrid() is called by eventHandler of the frame each 'delay' interval
     */
    private void initTimeline() {
        var frame = new KeyFrame(Duration.millis(delay), e -> updateGrid());
        this.myTimeline = new Timeline();
        this.myTimeline.setCycleCount(Timeline.INDEFINITE);
        this.myTimeline.getKeyFrames().add(frame);
    }

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
     * @return the 2D array of Cells for this simulation
     */
    public Cell[][] getGrid() {
        return this.myGrid;
    }


    /**
     * @return an immutable map associating state and corresponding image visualization
     */
    public Map<String, String> getStateImageMap(){
        return myConfig.getStateImageMap();
    }


    /**
     * Update all Cells' states in the grid
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
     * Public method for starting a new simulation
     * Expected to be called from IntroScene after user has selected a simulation model
     */
    public void startSimulation(){
        initSimulation();
        initUI();
        initTimeline();
    }


    /**
     * Pause the simulation
     * Expected to be called by UI when a pause button is pressed
     */
    public void pauseSimulation() {
        System.out.println("paused");
        this.myTimeline.stop();
    }


    /**
     * Run the simulation
     * Expected to be called by UI when a start/resume button is pressed
     */
    public void playSimulation() {
        this.myTimeline.play();
    }


    /**
     * Update Cell states to the next generation and then pause
     * Expected to be called by UI when a step button is pressed
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
        System.out.println("stopped for switching");
        this.setSimType(newSimType);
        initSimulation();
        initUI();
        resetDelay();
        initTimeline();
    }


    /**
     * Modify the simulation speed by adjusting delay time between frames based on the passed-in double
     * The double passed in is expected to be between 0 and 1
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
     * @return the current simulation model's XML file path
     */
    public String getSimulationType() {
        return this.SIM_TYPE;
    }


    /**
     * Main method to launch the Breakout game program.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
