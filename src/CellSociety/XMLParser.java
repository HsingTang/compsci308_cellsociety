package CellSociety;

import CellSociety.Exceptions.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.Map.entry;

/**
 * @author Robert C. Duvall
 * @author Rhondu Smithwick
 * @author Hsingchih Tang
 * Parser for XML files. Built based on XMLParser.java in sample project
 * Read the simulation's size, type and initial configuration parameters from certain files
 * Has private methods for parsing contents in the file and public methods for returning parsing results
 */
public class XMLParser {
    static final String SIM_TYPE_TAG = "Type";
    static final String WIDTH_TAG = "Width";
    static final String HEIGHT_TAG = "Height";
    static final String CONFIG_TAG = "SpecifiedConfig";
    static final String CELL_SHAPE_TAG = "CellShape";
    static final String CELL_NEIGHBOR_TAG = "NeighborStyle";
    static final String EDGE_TAG = "EdgeStyle";
    static final String STATE_TAG = "State";
    static final String STATE_NAME_TAG = "StateName";
    static final String STATE_IMG_TAG = "StateImage";
    static final String STATE_PERCENT_TAG = "StatePercentage";
    static final String PARAMETER_TAG = "Parameter";
    static final String CELL_TAG = "Cell";
    static final String CELL_ROW_TAG = "Row";
    static final String CELL_COL_TAG = "Col";
    static final String CELL_STATE_TAG = "CellState";
    private final Map<String, Integer> VALID_CELL_SHAPE_MAXNEIGHBOR = Map.ofEntries(
            entry("Square", 8),
            entry("Triangle", 12));
    private final List<String> VALID_EDGE_TYPE = List.of(
            "Finite",
            "Toroidal");

    // private variables for storing parsing results
    private DocumentBuilder myDBuilder;
    private String mySimulationType = "";
    private String myCellShape = "Square";
    private String myEdgeType = "Finite";
    private Element mySimRoot;
    private Integer myWidth;
    private Integer myHeight;
    private HashMap<String, String> stateImage = new HashMap<>();
    private HashMap<String, Double> statePercent = new HashMap<>();
    private HashMap<List<Integer>, String> cellState = new HashMap<>();
    private ArrayList<Double> parameters = new ArrayList<>();
    private ArrayList<Integer> neighbors = new ArrayList<>();
    private boolean specConfig = false;


    /**
     * Constructor of the XMLParser. Call parsing methods for different data as a pipeline.
     * Any Exception that occurs at the parser will be thrown to the caller class, and will
     * be eventually handled in the Simulation class.
     * @param f the specific model configuration file to parse
     * @throws SimulationException if any parsing step encounters missing/invalid data
     * @throws IOException if the file is not found
     * @throws ParserConfigurationException if the user's java environment does not support XMLParser
     * @throws SAXException if the XML file has invalid format
     */
    public XMLParser(File f) throws SimulationException, IOException, ParserConfigurationException,SAXException {
        try {
            this.myDBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw e;
        }
        try {
            this.mySimRoot = getRootElement(f);
        } catch (SAXException | IOException e) {
            throw e;
        }
        if (this.mySimRoot != null) {
            try{
                this.parseSimConfig();
                specConfig = this.parseSpecConfig();
                this.parseCellShape();
                this.parseEdgeType();
                this.parseCellNeighbor();
                this.parseState();
                this.parseParam();
                if (this.specConfig) {
                    this.parseCell();
                }
            }catch (SimulationException e){
                throw e;
            }
        }
    }


    /**
     * @return a string indicating the simulation type
     * Can be Fire, Game of Life, Percolation, Segregation, WaTor, RPS
     */
    public String getSimType() {
        return this.mySimulationType;
    }


    /**
     * @return String indicating the cell's visualization shape
     * Can be Rectangle or Triangle
     */
    public String getCellShape() {
        return this.myCellShape;
    }


    /**
     * @return String indicating the edge type
     * Can be Finite or Toroidal
     */
    public String getEdgeType() {
        return this.myEdgeType;
    }


    /**
     * @return immutable map indicating the visualization color for each state
     */
    public Map<String, String> getStateImg() {
        return Collections.unmodifiableMap(this.stateImage);
    }


    /**
     * @return immutable map indicating the percentage (if any) associated with each state in initial configuration
     */
    public Map<String, Double> getStatePercent() {
        return Collections.unmodifiableMap(this.statePercent);
    }


    /**
     * @return immutable list storing simulation-specific parameters
     * This list will be modifiable in order to to allow user interaction with the UI scene
     */
    public List<Double> getParameters() {
        return this.parameters;
    }


    /**
     * @return immutable list defining "neighbors" of a cell in the grid with location-based indices
     */
    public List<Integer> getNeighbors() {
        return Collections.unmodifiableList(this.neighbors);
    }


    /**
     * @return immutable list explicitly defining the initial state of each cell
     */
    public Map<List<Integer>, String> getCellState() {
        return Collections.unmodifiableMap(this.cellState);
    }


    /**
     * @return Integer indicating number of cells per row in the grid
     */
    public Integer getWidth() {
        return this.myWidth;
    }


    /**
     * @return Integer indicating number of cells per column in the grid
     */
    public Integer getHeight() {
        return this.myHeight;
    }


    /**
     * @return boolean flag indicating whether cells' initial states are explicitly defined in the XML file
     * Simulation will assign cell states completely randomly or based on distribution percentage if this flag is false
     */
    public boolean isSpecConfig() {
        return this.specConfig;
    }


    /**
     * Get root element of an XML file
     * @param xmlFile the target file to parse
     * @return the root element of the XML file if parsing success
     * @throws IOException if file not found
     * @throws SAXException if the XML file has invalid format
     */
    private Element getRootElement(File xmlFile) throws IOException, SAXException {
        Document myDoc;
        myDBuilder.reset();
        try {
            myDoc = myDBuilder.parse(xmlFile);
            return myDoc.getDocumentElement();
        }catch (SAXException e) {
            throw new SAXException("XML file is mal-formatted", e);
        } catch (IOException e) {
            throw e;
        }
    }


    /**
     * Parse the simulation's type, grid width and grid height
     * @throws ModelErrException if the simulation type information is missing
     * @throws GridErrException if the simulation's grid size information is missing
     */
    private void parseSimConfig() throws ModelErrException, GridErrException{
        NodeList simTypeNode = this.mySimRoot.getElementsByTagName(SIM_TYPE_TAG);
        if (simTypeNode.getLength() == 0) {
            throw new ModelErrException("Missing simulation type information");
        }
        NodeList widthNode = this.mySimRoot.getElementsByTagName(WIDTH_TAG);
        NodeList heightNode = this.mySimRoot.getElementsByTagName(HEIGHT_TAG);
        if (widthNode.getLength() == 0 || heightNode.getLength() == 0) {
            throw new GridErrException("Missing simulation grid size information");
        }
        mySimulationType = simTypeNode.item(0).getTextContent();
        myWidth = Integer.valueOf(widthNode.item(0).getTextContent());
        myHeight = Integer.valueOf(heightNode.item(0).getTextContent());
    }


    /**
     * Parse the flag indicating whether cells initial states are explicitly defined in file
     * or should be randomly generated based on distribution percentage
     * @return boolean value indicating whether cell initial states are specifically defined in file
     */
    /**
     * Parse the flag indicating whether cells initial states are explicitly defined in the file
     * or should be randomly generated based on distribution percentage (if provided in file)
     * @return boolean value indicating whether cell initial states are specifically defined in file
     * @throws SpecErrException if the specification information is missing
     */
    private boolean parseSpecConfig() throws SpecErrException{
        NodeList specNode = this.mySimRoot.getElementsByTagName(CONFIG_TAG);
        if (specNode.getLength() == 0) {
            throw new SpecErrException("Missing cell parsing specification info");
        }
        return Boolean.valueOf(specNode.item(0).getTextContent());
    }


    /**
     * Parse the cell's visualization shape: Rectangle or Triangle
     * Defaulted to Rectangle if no information is provided
     */
    private void parseCellShape() {
        NodeList shapeNode = this.mySimRoot.getElementsByTagName(CELL_SHAPE_TAG);
        if (shapeNode.getLength() != 0) {
            String shape = shapeNode.item(0).getTextContent();
            if (VALID_CELL_SHAPE_MAXNEIGHBOR.keySet().contains(shape)) {
                myCellShape = shape;
            }
        }
    }


    /**
     * Parse the grid's edge type: Finite or Toroidal
     * Defaulted to Finite if no information is provided
     */
    private void parseEdgeType() {
        NodeList edgeNode = this.mySimRoot.getElementsByTagName(EDGE_TAG);
        if (edgeNode.getLength() != 0) {
            String edge = edgeNode.item(0).getTextContent();
            if (VALID_EDGE_TYPE.contains(edge)) {
                myEdgeType = edge;
            }
        }
    }


    /**
     * Parse the configuration of neighboring cells for a cell
     * Neighbors are numbered by integers (0-7 for Rectangle shape; 0-11 for Triangle shape) and stored in a list
     * @throws NeighborErrException if the neighbor indices provided is invalid (out of bound) or missing
     */
    private void parseCellNeighbor() throws NeighborErrException{
        NodeList neighborNode = this.mySimRoot.getElementsByTagName(CELL_NEIGHBOR_TAG);
        if (neighborNode.getLength() != 0) {
            String[] neighborsInString = neighborNode.item(0).getTextContent().split(";");
            for (String s : neighborsInString) {
                Integer neighborIdx = Integer.valueOf(s);
                if (neighborIdx >= VALID_CELL_SHAPE_MAXNEIGHBOR.get(myCellShape)) {
                    throw new NeighborErrException("Invalid neighbor indices provided");
                }
                neighbors.add(neighborIdx);
            }
        } else {
            throw new NeighborErrException("Missing cell neighbor information");
        }
    }


    /**
     * Parse the names, associated visualization colors, and percentage distribution of the states in this simulation
     * @throws StateErrException if any information regarding the model's states is missing or invalid
     */
    private void parseState() throws StateErrException{
        NodeList stateList = this.mySimRoot.getElementsByTagName(STATE_TAG);
        if (stateList.getLength() == 0) {
            throw new StateErrException("Missing state information");
        }
        for (int i = 0; i < stateList.getLength(); i++) {
            Node stateNode = stateList.item(i);
            NodeList currName = ((Element) stateNode).getElementsByTagName(STATE_NAME_TAG);
            NodeList currImg = ((Element) stateNode).getElementsByTagName(STATE_IMG_TAG);
            if (currImg.getLength() == 0 || currName.getLength() == 0) {
                throw new StateErrException("Missing state image");
            }
            String currStateName = currName.item(0).getTextContent();
            stateImage.put(currStateName, currImg.item(0).getTextContent());

            NodeList currPercent = ((Element) stateNode).getElementsByTagName(STATE_PERCENT_TAG);
            if (currPercent.getLength() != 0) {
                statePercent.put(currStateName, Double.valueOf(currPercent.item(0).getTextContent()));
            }
        }
        if (stateImage.keySet().size() != statePercent.keySet().size() && !statePercent.keySet().isEmpty()) {
            throw new StateErrException("State count does not match percentage information provided");
        }
    }


    /**
     * Parse any possible parameter for this simulation (e.g. threshold, cell reproduce time, etc.)
     */
    private void parseParam() {
        NodeList paramList = this.mySimRoot.getElementsByTagName(PARAMETER_TAG);
        for (int i = 0; i < paramList.getLength(); i++) {
            Node paramNode = paramList.item(i);
            parameters.add(Double.valueOf(paramNode.getTextContent()));
        }
    }


    /**
     * Parse cell's initial state if specConfig flag is raised
     * All cells' states are stored in a map and associated with row/column indices
     * @throws CellSpecException if the previously parsed section has indicated that cell states
     * shall be explicitly defined in file, but no cell state information is provided in this section
     * @throws CellIdxException if cell index out of bound with respect to the grid size parsed in previous section
     * @throws CellStateException if specified cell state does not match state configuration parsed in previous section
     * @throws CellInfoException if initial state information of any cell in the grid is missing
     */
    private void parseCell() throws CellSpecException, CellIdxException, CellStateException,CellInfoException{
        NodeList cellList = this.mySimRoot.getElementsByTagName(CELL_TAG);
        if (cellList.getLength() == 0) {
            throw new CellSpecException("File parsing style does not match specific cell info provided");
        }
        for (int i = 0; i < cellList.getLength(); i++) {
            Node currCellNode = cellList.item(i);
            if (!validateCellInfo(currCellNode)) {
                throw new CellInfoException("Missing cell information");
            }
            int currRow = Integer.valueOf(((Element) currCellNode).getElementsByTagName(CELL_ROW_TAG).item(0).getTextContent());
            int currCol = Integer.valueOf(((Element) currCellNode).getElementsByTagName(CELL_COL_TAG).item(0).getTextContent());
            if (!validateCellIdx(currRow, currCol)) {
                throw new CellIdxException("Cell index out of bounds");
            }
            String currState = ((Element) currCellNode).getElementsByTagName(CELL_STATE_TAG).item(0).getTextContent();
            if (!this.stateImage.containsKey(currState)) {
                throw new CellStateException("Invalid cell state configuration");
            }
            cellState.put(Arrays.asList(currRow, currCol), currState);
        }
        if (cellState.keySet().size() != myWidth * myHeight) {
            throw new CellInfoException("Cell count does not match grid width/height configuration");
        }
    }


    /**
     * Check whether cell's row/column indices and initial state are all given
     * @param cellNode the current cell to validate
     * @return boolean value indicating information valid or not
     */
    private boolean validateCellInfo(Node cellNode) {
        return !(((Element) cellNode).getElementsByTagName(CELL_ROW_TAG).getLength() == 0
                || ((Element) cellNode).getElementsByTagName(CELL_COL_TAG).getLength() == 0
                || ((Element) cellNode).getElementsByTagName(CELL_STATE_TAG).getLength() == 0);
    }


    /**
     * Check whether cell's row/column indices are out of bounds of the grid
     * @param row row index of the cell
     * @param col column index of the cell
     * @return boolean value indicating validity of the cell's indices
     */
    private boolean validateCellIdx(int row, int col) {
        return row >= 0 && col >= 0 && row < myHeight && col < myWidth;
    }

}
