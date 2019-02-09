package CellSociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.*;

import static java.lang.Math.ceil;
import static java.util.Map.entry;

/**
 * @author Hsingchih Tang
 * Trunk of the cell society project
 * Control the simulation flow by invoking and connecting XMLParser, IntroScene, UI and Cell classes
 * Retrieve simulation configuration parameters from XMLParser and pass arguments into UI and Cells
 * Respond to user action of playing/resuming/stepping/switching simulation
 */
public class Simulation extends Application {

    public static final int DEFAULT_WIDTH = 20;
    public static final int DEFAULT_HEIGHT = 20;
    public static final String GOL_XML = "Game of Life";
    public static final String WATOR_XML = "WaTor";
    public static final String FIRE_XML = "RPS";
    public static final String SEG_XML = "Segregation";
    public static final String PERC_XML = "Percolation";
    protected final List<String> SIM_TYPE_LIST = Arrays.asList(GOL_XML,WATOR_XML,FIRE_XML,SEG_XML,PERC_XML);
    protected final Map<String,Integer> SIM_PARAM_NUM = Map.ofEntries(
            entry(GOL_XML,0),
            entry(WATOR_XML,4),
            entry(FIRE_XML,1),
            entry(SEG_XML,1),
            entry(PERC_XML,0)
    );

    protected final Map<String,Integer> SIM_STATE_NUM = Map.ofEntries(
            entry(GOL_XML,2),
            entry(WATOR_XML,3),
            entry(FIRE_XML,3),
            entry(SEG_XML,3),
            entry(PERC_XML,3)
    );

    private Timeline myTimeline;
    private Stage myStage;
    private double delay = 1000;
    private Cell[][] myGrid;
    private Scene myIntroScene;
    private UI myUIScene;
    private int myWidth = DEFAULT_WIDTH;
    private int myHeight = DEFAULT_HEIGHT;
    private String SIM_TYPE;
    private boolean specConfig = false;
    // private boolean pause = false;
    // private boolean step = true;
    private Map<String, String> stateImageMap;
    private Map<String, Double> statePercentMap;
    private List<Double> parametersList;
    private List<String> stateList;
    private Map<List<Integer>,String> cellState;
    private XMLParser myParser;

    public Simulation(){
        // constructor, do something here
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
     * Initialize the introduction scene where user can choose type of simulation to run
     * Set stage to the initialized IntroScene
     */
    private void initIntroScene() {
        Group myIntroRoot = new Group();
        myIntroScene = new IntroScene(myIntroRoot, DEFAULT_WIDTH, DEFAULT_HEIGHT, this);
        myStage.setScene(myIntroScene);
        myStage.setTitle("Cell Society");
        myStage.show();
    }


    /**
     * External classes can call this method to change the simulation type
     * The corresponding XML file should be available in resources folder
     * @param s name of the new simulation's XML file
     */
    public void setSimType(String s) {
        this.SIM_TYPE = s;
    }


    /**
     * Initialize a list of states with/without the percentage distribution read from XML file
     */
    private void initStateList() {
        this.stateList = new ArrayList<>();
        if(statePercentMap.size()==0){
            for (String state : stateImageMap.keySet()) {
                stateList.add(state);
            }
        }else{
            for (String state : statePercentMap.keySet()) {
                for (int i = 0; i < ceil(statePercentMap.get(state) * 100); i++) {
                    stateList.add(state);
                }
            }
        }
    }



    /**
     * This is a public method that's expected to be called from UI class each time a new simulation is initialized
     * Initialize the grid of Cells and set each Cell's initial state
     * and then pipeline to the next step of creating UI scene for displaying visualization
     */
    public void initGrid() {
        if(!readXML()){
            return;
        }
        myGrid = new Cell[myHeight][myWidth];
        initStateList();
        Random myRandom = new Random();
        for (int i = 0; i < myGrid.length; i++) {
            for (int j = 0; j < myGrid[0].length; j++) {
                Cell currCell = null;
                int randIdx = myRandom.nextInt(stateList.size());
                String currCellState;
                if(this.specConfig){
                    List<Integer> cellIdx = Arrays.asList(i,j);
                    currCellState = cellState.get(cellIdx);
                }else{
                    currCellState = stateList.get(randIdx);
                }
                ArrayList<Double> params = new ArrayList<>(parametersList);
                switch (SIM_TYPE) {
                    case GOL_XML:
                        currCell = new CellGameOfLife(i, j, currCellState, params);
                        break;
                    case WATOR_XML:
                        currCell = new CellWATOR(i, j, currCellState, params);
                        break;
                    case FIRE_XML:
                        currCell = new CellFire(i, j, currCellState, params);
                        break;
                    case SEG_XML:
                        currCell = new CellSegregation(i, j, currCellState, params);
                        break;
                    case PERC_XML:
                        currCell = new CellPercolation(i, j, currCellState, params);
                        break;
                }
                myGrid[i][j] = currCell;
            }
        }
        for (int i = 0; i < myGrid.length; i++) {
            for (int j = 0; j < myGrid[0].length; j++) {
                myGrid[i][j].findNeighbors(myGrid);
            }
        }
        initUI();
    }



    /**
     * Initialize the UI class for creating visualization of the simulation
     */
    private void initUI() {
        Group myUIRoot = new Group();
        myUIScene = new UI(myUIRoot, myWidth, myHeight, this);
        myUIScene.drawGrid();
        myStage.setScene(myUIScene);
        myStage.show();
        initTimeline();
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


    /**
     * Check for error in XML parsing results. Terminate further grid initialization if invalid.
     * @param parser XMLParser object which handled the input file
     * @return boolean value indicating whether the parsed information is valid
     */
    public boolean validateSimulation(XMLParser parser){
        if(!SIM_TYPE_LIST.contains(parser.getSimType())){
            myParser.modelErrAlert.showAlert();
            return false;
        }else if(SIM_PARAM_NUM.get(parser.getSimType())!=parser.getParameters().size()){
            myParser.paramErrAlert.showAlert();
            return false;
        }else if(SIM_STATE_NUM.get(parser.getSimType())!=parser.getStateImg().keySet().size()){
            myParser.stateErrAlert.showAlert();
            return false;
        }else if(!parser.isParseSuccess()){
            return false;
        }
        return true;
    }



    /**
     * Read XML file containing simulation parameters
     */
    private boolean readXML() {
        String myFilePath;
        if(SIM_TYPE_LIST.contains(SIM_TYPE)){
            myFilePath = "resources/"+SIM_TYPE+".xml";
        }else{
            myFilePath = SIM_TYPE;
        }
        File f = new File(myFilePath);
        try{
            myParser= new XMLParser(f);
        }catch (Exception e){
            myParser.parserConfigAlert.showAlert();
            return false;
        }
        if(!validateSimulation(myParser)) {
            return false;
        }
        this.stateImageMap = myParser.getStateImg();
        this.statePercentMap = myParser.getStatePercent();
        this.parametersList = myParser.getParameters();
        this.myWidth = myParser.getWidth();
        this.myHeight = myParser.getHeight();
        this.specConfig = myParser.isSpecConfig();
        this.cellState = myParser.getCellState();
        return true;
    }


    /**
     * @return the 2D array of Cells for this simulation
     */
    public Cell[][] getGrid() {
        return this.myGrid;
    }


    /**
     * @return a map associating state and corresponding image visualization
     */
    public Map<String, String> getStateImageMap(){
        return this.stateImageMap;
    }


    /**
     * Update all Cells' states in the grid
     */
    private void updateGrid() {
        for (int i = 0; i < myGrid.length; i++) {
            for (int j = 0; j < myGrid[0].length; j++) {
                Cell currCell = myGrid[i][j];
                currCell.findNextState();
            }
        }
        for (int i = 0; i < myGrid.length; i++) {
            for (int j = 0; j < myGrid[0].length; j++) {
                Cell currCell = myGrid[i][j];
                currCell.updateState();
            }
        }
        this.myUIScene.drawGrid();
    }


    /**
     * Pause the simulation
     * Expected to be called by UI when a pause button is pressed
     */
    public void pauseSimulation() {
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
    public void resetSimulation() {
        this.myTimeline.pause();
        initGrid();
    }


    /**
     * Switch to the other simulation model by reading the corresponding XML file
     * and reinitializing all Cells in the grid
     * @param newSimType path to the XML file for the new Simulation
     */
    public void switchSimulation(String newSimType) {
        this.myTimeline.stop();
        this.setSimType(newSimType);
        initGrid();
    }


    /**
     * Slow down the simulation by increasing the delay time interval by twice
     */
    public void slowdown() {
        this.myTimeline.stop();
        this.delay *= 2;
        initTimeline();
        playSimulation();
    }


    /**
     * Speed up the simulation by reducing the delay time interval to its half
     */
    public void speedup() {
        this.myTimeline.stop();
        this.delay /= 2;
        initTimeline();
        playSimulation();
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
