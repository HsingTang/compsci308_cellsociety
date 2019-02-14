package CellSociety;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Math.ceil;

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


    public Configurator() throws FileNotFoundException{
        try{
            readConfig();
        }catch (FileNotFoundException e){
            System.out.println("Simulation configuration file not found.");
            throw e;
        }
    }


    public double getMinDelay(){
        return this.minDelay;
    }


    public double getMaxDelay(){
        return this.maxDelay;
    }

    public int getWidth(){
        return this.configWidth;
    }

    public int getHeight(){
        return this.configHeight;
    }

    public String getTitle(){
        return this.myTitle;
    }

    public String getCellShape(){
        return this.cellShape;
    }

    public String getSimType(){
        return this.SIM_TYPE;
    }

    public List<Double> getParamList(){
        return this.parametersList;
    }

    public Map<String, String> getStateImageMap(){
        return Collections.unmodifiableMap(this.stateImageMap);
    }

    public Cell[][] getGrid(){
        return this.myGrid;
    }

    public void setConfigSimType(String s){
        this.SIM_TYPE = s;
    }



    /**
     * Read the configuration text file for basic parameters (default size, simulation delay, etc.) in Simulation
     * @throws FileNotFoundException if the configuration file is not found
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
     * Check for error in XML parsing results. Terminate further grid initialization if invalid.
     * @param parser XMLParser object which handled the input file
     * @return boolean value indicating whether the parsed information is valid
     */
    private boolean validateSimulation(XMLParser parser){
        if(!SIM_TYPE_LIST.contains(parser.getSimType())){
            myParser.modelErrAlert.showAlert();
            return false;
        }else if(SIM_PARAM_NUM.get(parser.getSimType())!=parser.getParameters().size()){
            myParser.paramErrAlert.showAlert();
            return false;
        }else if(SIM_STATE_NUM.get(parser.getSimType())!=parser.getStateImg().keySet().size()){
            myParser.stateErrAlert.showAlert();
            return false;
        }
        return parser.isParseSuccess();
    }


    /**
     * A private method that's expected to be called from switchSimulation() or resetSimulation()
     * Initialize the grid of cells of a specific concrete cell class and set each cell's initial state
     * Then pipeline to the next step of creating UI scene for displaying visualization
     */
    public Cell[][] initGrid() throws Exception{
        try{
            readXML();
        }
        catch (Exception e){
            myParser.parserConfigAlert.showAlert();
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
     * Initialize a list of states with/without the percentage distribution read from XML file
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
        int i = 0;
        for (Cell[] row :myGrid) {
            int j = 0;
            for (Cell currCell:row) {
                currCell.findNeighbors(myGrid,cellShape,edgeType,neighborList);
                j++;
            }
            i++;
        }
    }


    /**
     * Read XML file containing simulation parameters
     */
    private boolean readXML() throws Exception{
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
            throw e;
        }
        if(!validateSimulation(myParser)) {
            return false;
        }
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
        return true;
    }





}
