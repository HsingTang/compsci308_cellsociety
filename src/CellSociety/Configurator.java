package CellSociety;

import CellSociety.Exceptions.ModelErrException;
import CellSociety.Exceptions.ParamErrException;
import CellSociety.Exceptions.StateErrException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.ceil;

/**
 * @author Hsingchih Tang
 * Core of the cell society project. Establish connection between Simulation and XMLParser for configuring
 * the simulation with information parsed from text or xml files.
 *
 * Code Refactoring:
 * This is a new class split from the original Simulation class, and serves as the pure back-end of the whole project.
 * None of its mothods implements front-end effects (in this project, accomplished via JavaFX), so this class
 * can be completely closed and still be compatible to different front-ends.
 *
 * Any parsing exception or file mal-formatting issue will be thrown out to Simulation class, who handles the Exceptions
 * by popping up alert dialogue boxes to user.
 */
public class Configurator {
    static final String configFilePath = "resources/SimulationConfig.txt";
    static final String GOL_XML = "Game of Life";
    static final String WATOR_XML = "WaTor";
    static final String FIRE_XML = "Fire";
    static final String SEG_XML = "Segregation";
    static final String PERC_XML = "Percolation";
    static final String RPS_XML = "Rock Paper Scissors";

    private int configWidth;
    private int configHeight;
    private double minDelay;
    private double maxDelay;
    private double distributionAccuracy;
    private String myTitle;
    private List<String> SIM_TYPE_LIST = new ArrayList<>();
    private Map<String,Integer> SIM_PARAM_NUM = new HashMap<>();
    private Map<String,Integer> SIM_STATE_NUM = new HashMap<>();
    private String SIM_TYPE;
    private String cellShape;
    private String edgeType;
    private boolean specConfig = false;
    private List<String> stateList;
    private List<Double> parametersList;
    private List<Integer> neighborList;
    private Map<String, String> stateImageMap;
    private Map<String, Double> statePercentMap;
    private Map<List<Integer>,String> cellStateMap;
    private XMLParser myParser;
    private Cell[][] myGrid;


    /**
     * Constructor of Configurator
     * Reads in the general simulation configuration file (not specific to any model), which contains information about
     * valid model types, associated number of states and parameters, as well as default simulation speed and grid size
     * @throws FileNotFoundException if the general configuration file is missing
     */
    public Configurator() throws FileNotFoundException{
        try{
            readConfig();
        }catch (FileNotFoundException e){
            System.out.println("General simulation configuration file not found.");
            throw e;
        }
    }


    /**
     * A public method that's expected to be called from Simulation class
     * Initialize the grid of cells of a specific concrete cell class and set each cell's initial state
     * @return a 2-D array of cells of a concrete cell class
     * @throws SimulationException if readXML() encounters invalid/missing data during parsing
     * @throws IOException if the simulation model's configuration file is not found by the XMLParser
     * @throws ParserConfigurationException if the user's java environment does not support XMLParser
     * @throws SAXException if the XML file format is invalid
     */
    public Cell[][] initGrid() throws SimulationException, IOException, ParserConfigurationException,SAXException{
        try{
            readXML();
        }
        catch (SimulationException|IOException|ParserConfigurationException|SAXException e){
            throw e;
        }
        myGrid = new Cell[configHeight][configWidth];
        initStateList();
        for (int i = 0; i < myGrid.length; i++) {
            for (int j = 0; j < myGrid[0].length; j++) {
                Cell currCell = null;
                String currCellState = defineState(i,j);
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
                    case RPS_XML:
                        currCell = new CellRPS(i, j, currCellState, params);
                        break;
                }
                myGrid[i][j] = currCell;
            }
        }
        initNeighbors();
        return myGrid;
    }


    /**
     * @return the default minimum delay value for setting up animation frame in Simulation
     */
    public double getMinDelay(){
        return this.minDelay;
    }


    /**
     * @return the default maximum delay value for setting up animation frame in Simulation
     */
    public double getMaxDelay(){
        return this.maxDelay;
    }


    /**
     * @return the default grid width for setting up IntroScene and UI in Simulation
     */
    public int getWidth(){
        return this.configWidth;
    }


    /**
     * @return the default grid height for setting up IntroScene and UI in Simulation
     */
    public int getHeight(){
        return this.configHeight;
    }


    /**
     * @return the title to display on the application window when program is launched
     */
    public String getTitle(){
        return this.myTitle;
    }


    /**
     * @return the cell shape specified in model configuration file for setting up UI visualization
     */
    public String getCellShape(){
        return this.cellShape;
    }


    /**
     * @return the list of parameters specified in model configuration file for setting up UI visualization
     * This method is intended to return the modifiable object so that UI can change the parameters based on user action
     */
    public List<Double> getParamList(){
        return this.parametersList;
    }


    /**
     * @return immutable map indicating the visualization color for each state for setting up UI visualization
     */
    public Map<String, String> getStateImageMap(){
        return Collections.unmodifiableMap(this.stateImageMap);
    }


    /**
     * @return the 2D array of Cells for this simulation
     * Want both Simulation and UI to have direct access to the cell objects,
     * so this method returns the exact original 2D cell array created and stored in Configurator.
     */
    public Cell[][] getGrid(){
        return this.myGrid;
    }


    /**
     * Allows outside class to change the simulation type, which affects the choice of model config file to parse
     * @param s the new simulation type
     */
    public void setConfigSimType(String s){
        this.SIM_TYPE = s;
    }


    /**
     * Read the configuration text file for general parameters (default size, simulation delay, etc.) in Simulation
     * @throws FileNotFoundException if the general configuration file is not found
     */
    private void readConfig() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(configFilePath));
        myTitle = sc.nextLine();
        configWidth = Integer.valueOf(sc.nextLine());
        configHeight = Integer.valueOf(sc.nextLine());
        distributionAccuracy = Double.valueOf(sc.nextLine());
        minDelay = Double.valueOf(sc.nextLine());
        maxDelay = Double.valueOf(sc.nextLine());
        while(sc.hasNextLine()){
            String modelName = sc.nextLine();
            Integer paramNum = Integer.valueOf(sc.nextLine());
            Integer stateNum = Integer.valueOf(sc.nextLine());
            SIM_TYPE_LIST.add(modelName);
            SIM_PARAM_NUM.put(modelName,paramNum);
            SIM_STATE_NUM.put(modelName,stateNum);
        }
    }


    /**
     * Read XML file containing specific configuration data for simulation model
     * @throws SimulationException if encountering invalid/missing data during parsing
     * @throws IOException if the simulation model's configuration file is missing
     * @throws ParserConfigurationException if the user's java environment does not support XMLParser
     * @throws SAXException if the XML file format is invalid
     */
    private void readXML() throws SimulationException, IOException, ParserConfigurationException,SAXException{
        String myFilePath;
        if(SIM_TYPE_LIST.contains(SIM_TYPE)){
            myFilePath = "resources/"+SIM_TYPE+".xml";
        }else{
            myFilePath = SIM_TYPE;
        }
        File f = new File(myFilePath);
        try{
            myParser= new XMLParser(f);
        }catch (SimulationException|IOException|ParserConfigurationException|SAXException e){
            throw e;
        }
        boolean isValid;
        try{
            isValid = validateSimulation(myParser);
        } catch (SimulationException e){
            throw e;
        }
        if(isValid){
            this.configWidth = myParser.getWidth();
            this.configHeight = myParser.getHeight();
            this.specConfig = myParser.isSpecConfig();
            this.cellShape = myParser.getCellShape();
            this.edgeType = myParser.getEdgeType();
            this.neighborList = myParser.getNeighbors();
            this.parametersList = myParser.getParameters();
            this.stateImageMap = myParser.getStateImg();
            this.statePercentMap = myParser.getStatePercent();
            this.cellStateMap = myParser.getCellState();
        }
    }


    /**
     * Validate the XML parsing results with information previously read from general simulation configuration file
     * @param parser XMLParser object who parsed the input file
     * @return boolean value indicating whether the parsed information is valid
     * @throws ModelErrException if the parsed simulation type is not a valid model type
     * @throws ParamErrException if the number of parameters is incorrect for the certain model
     * @throws StateErrException if the number of states is incorrect for the certain model
     */
    private boolean validateSimulation(XMLParser parser) throws ModelErrException, ParamErrException, StateErrException {
        if(!SIM_TYPE_LIST.contains(parser.getSimType())){
            throw new ModelErrException("Invalid simulation type");
        }else if(SIM_PARAM_NUM.get(parser.getSimType())!=parser.getParameters().size()){
            throw new ParamErrException("Invalid parameter configuration for current simulation "+SIM_TYPE);
        }else if(SIM_STATE_NUM.get(parser.getSimType())!=parser.getStateImg().keySet().size()){
            throw new StateErrException("Invalid state configuration for current simulation "+SIM_TYPE);
        }
        return true;
    }


    /**
     * Initialize a list of states with or without the percentage distribution read from XML file
     */
    private void initStateList() {
        this.stateList = new ArrayList<>();
        if(statePercentMap.size()==0){
            stateList.addAll(stateImageMap.keySet());
        }else{
            for (Map.Entry<String,Double> entry : statePercentMap.entrySet()) {
                for (int i = 0; i < ceil(entry.getValue() * distributionAccuracy); i++) {
                    stateList.add(entry.getKey());
                }
            }
        }
    }


    /**
     * Generate state for the current cell based on simulation configuration read from XML file
     * @param row Row index of the cell
     * @param col Column index of the cell
     * @return a String indicating the cell's initial state
     */
    private String defineState(int row, int col){
        Random myRandom = new Random();
        int randIdx = myRandom.nextInt(stateList.size());
        if(this.specConfig){
            List<Integer> cellIdx = Arrays.asList(row,col);
            return cellStateMap.get(cellIdx);
        }else{
            return stateList.get(randIdx);
        }
    }


    /**
     * Loop through all cells in the grid and initialize neighbors for each cell
     */
    private void initNeighbors(){
        for (Cell[] row :myGrid) {
            for (Cell currCell:row) {
                currCell.findNeighbors(myGrid,cellShape,edgeType,neighborList);
            }
        }
    }

}
