package CellSociety;

import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

/**
 * @author Robert C. Duvall
 * @author Rhondu Smithwick
 * @author Hsingchih Tang
 * Parser for XML files. Built based on XMLParser.java and XMLException.java in sample project
 * Read the simulation's size, type and initial configuration parameters from certain files
 * Has private methods for parsing contents in the file
 * and public methods for returning information to caller
 */
public class XMLParser {
    public static final String SIM_TYPE_TAG = "Type";
    public static final String TITLE_TAG = "Title";
    public static final String AUTHOR_TAG = "Author";
    public static final String WIDTH_TAG = "Width";
    public static final String HEIGHT_TAG = "Height";
    public static final String CONFIG_TAG = "SpecifiedConfig";
    public static final String STATE_TAG = "State";
    public static final String STATE_NAME_TAG = "StateName";
    public static final String STATE_IMG_TAG = "StateImage";
    public static final String STATE_PERCENT_TAG = "StatePercentage";
    public static final String PARAMETER_TAG = "Parameter";
    public static final String CELL_TAG = "Cell";
    public static final String CELL_ROW_TAG = "Row";
    public static final String CELL_COL_TAG = "Col";
    public static final String CELL_STATE_TAG = "CellState";
    private File myFile;
    private DocumentBuilder myDBuilder;
    private Document myDoc;
    private String mySimulationType = "";
    private String mySimulationTitle = "";
    private String myAuthor = "";
    private Element mySimRoot;
    private Integer myWidth;
    private Integer myHeight;
    private HashMap<String, String> stateImage = new HashMap<>();
    private HashMap<String, Double> statePercent = new HashMap<>();
    private HashMap<List<Integer>,String> cellState = new HashMap<>();
    private ArrayList<Double> parameters = new ArrayList<>();
    private XMLAlert myAlert;
    private boolean specConfig = false;
    private boolean parseSuccess = true;



    public XMLParser(File f){
        this.myFile = f;
        try{
            this.myDBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch (ParserConfigurationException e){
            // Error case: ParserConfigurationException handling
            this.myAlert = XMLAlert.ParserConfigAlert;
            this.myAlert.showAlert();
            return;
        }
        this.mySimRoot = getRootElement(f);
        if(this.mySimRoot!=null){
            this.parseSimConfig();
            this.parseSpecConfig();
            this.parseTitle();
            this.parseAuthor();
            this.parseState();
            this.parseParam();
            this.parseCell();
        }

    }

    // Get root element of an XML file
    private Element getRootElement (File xmlFile) {
        myDBuilder.reset();
        try {
            this.myDoc = myDBuilder.parse(xmlFile);
            return this.myDoc.getDocumentElement();
        }
        // Error case: exception handling
        catch (SAXException e) {
            this.myAlert = XMLAlert.SAXAlert;
            this.myAlert.showAlert();
            this.parseSuccess = false;
        }catch(IOException e){
            this.myAlert = XMLAlert.FileNotFoundAlert;
            this.myAlert.showAlert();
            this.parseSuccess = false;
        }
        return null;
    }


    private void parseSimConfig(){
        NodeList simTypeNode = this.mySimRoot.getElementsByTagName(SIM_TYPE_TAG);
        NodeList widthNode = this.mySimRoot.getElementsByTagName(WIDTH_TAG);
        NodeList heightNode = this.mySimRoot.getElementsByTagName(HEIGHT_TAG);
        // Error case: missing simulation type information
        if(simTypeNode.getLength()==0){
            this.myAlert = XMLAlert.SimTypeAlert;
            this.myAlert.showAlert();
            this.parseSuccess = false;
            return;
        }
        // Error case: missing simulation grid size information
        if(widthNode.getLength()==0 || heightNode.getLength()==0){
            this.myAlert = XMLAlert.GridSizeAlert;
            this.myAlert.showAlert();
            this.parseSuccess = false;
            return;
        }
        mySimulationType = simTypeNode.item(0).getTextContent();
        myWidth = Integer.valueOf(widthNode.item(0).getTextContent());
        myHeight = Integer.valueOf(heightNode.item(0).getTextContent());
    }


    private void parseTitle(){
        NodeList titleNode = this.mySimRoot.getElementsByTagName(TITLE_TAG);
        if(titleNode.getLength()!=0){
            mySimulationTitle = titleNode.item(0).getTextContent();
        }
    }


    private void parseAuthor(){
        NodeList authorNode = this.mySimRoot.getElementsByTagName(AUTHOR_TAG);
        if(authorNode.getLength()!=0){
            myAuthor = authorNode.item(0).getTextContent();
        }
    }


    private void parseSpecConfig(){
        NodeList specNode = this.mySimRoot.getElementsByTagName(CONFIG_TAG);
        // Error case: Missing file parsing specification info
        if(specNode.getLength()==0){
            this.myAlert = XMLAlert.SpecConfigAlert;
            this.myAlert.showAlert();
            this.parseSuccess = false;
            return;
        }
        if(specNode.item(0).getTextContent().equals("true")){
            this.specConfig = true;
        }else{
            this.specConfig = false;
        }
    }


    private void parseState(){
        NodeList stateList = this.mySimRoot.getElementsByTagName(STATE_TAG);
        // Error case: missing state information
        if(stateList.getLength()==0){
            this.myAlert = XMLAlert.SimStateAlert;
            this.myAlert.showAlert();
            this.parseSuccess = false;
            return;
        }
        for(int i = 0; i<stateList.getLength(); i++){
            Node stateNode = stateList.item(i);
            NodeList currName = ((Element)stateNode).getElementsByTagName(STATE_NAME_TAG);
            NodeList currImg = ((Element)stateNode).getElementsByTagName(STATE_IMG_TAG);
            NodeList currPercent = ((Element)stateNode).getElementsByTagName(STATE_PERCENT_TAG);
            // Error case: missing image for the specified state
            if(currImg.getLength()==0 || currName.getLength()==0){
                this.myAlert = XMLAlert.SimStateAlert;
                this.myAlert.showAlert();
                this.parseSuccess = false;
                return;
            }
            String currStateName = currName.item(0).getTextContent();
            stateImage.put(currStateName,currImg.item(0).getTextContent());
            if(currPercent.getLength()!=0){
                statePercent.put(currStateName,Double.valueOf(currPercent.item(0).getTextContent()));
            }
        }
        // Error case: number of states does not match state percentage map size
        if(stateImage.keySet().size()!=statePercent.keySet().size() && statePercent.keySet().size()!=0){
            this.myAlert = XMLAlert.SimStateAlert;
            this.myAlert.showAlert();
            this.parseSuccess = false;
        }
    }


    private void parseParam(){
        NodeList paramList = this.mySimRoot.getElementsByTagName(PARAMETER_TAG);
        for(int i = 0; i<paramList.getLength(); i++){
            Node paramNode = paramList.item(i);
            parameters.add(Double.valueOf(paramNode.getTextContent()));
        }
    }


    private void parseCell(){
        NodeList cellList = this.mySimRoot.getElementsByTagName(CELL_TAG);
        // Error case: file parsing specification does not match cell info
        if((cellList.getLength()!=0 && this.specConfig==false)||(cellList.getLength()==0 && this.specConfig==true)){
            this.myAlert = XMLAlert.CellConfigConflictAlert;
            this.myAlert.showAlert();
            this.parseSuccess = false;
            return;
        }else if(this.specConfig==false){
            return;
        }
        for(int i = 0; i<cellList.getLength(); i++){
            Node cellNode = cellList.item(i);
            // Error case: missing cell information
            if(((Element)cellNode).getElementsByTagName(CELL_ROW_TAG).getLength()==0
                    || (((Element)cellNode).getElementsByTagName(CELL_COL_TAG).getLength()==0)
                    || ((Element)cellNode).getElementsByTagName(CELL_STATE_TAG).getLength()==0){
                this.myAlert = XMLAlert.CellInfoAlert;
                this.myAlert.showAlert();
                this.parseSuccess = false;
                return;
            }
            int currRow = Integer.valueOf(((Element)cellNode).getElementsByTagName(CELL_ROW_TAG).item(0).getTextContent());
            int currCol = Integer.valueOf(((Element)cellNode).getElementsByTagName(CELL_COL_TAG).item(0).getTextContent());
            // Error case: cell index out of bounds
            if(currRow<0 || currCol<0 || currRow>=myHeight || currCol>=myWidth){
                this.myAlert = XMLAlert.CellLocationAlert;
                this.myAlert.showAlert();
                this.parseSuccess = false;
                return;
            }
            String currState = ((Element)cellNode).getElementsByTagName(CELL_STATE_TAG).item(0).getTextContent();
            // Error case: invalid cell state configuration
            if(!this.stateImage.containsKey(currState)){
                this.myAlert = XMLAlert.CellStateAlert;
                this.myAlert.showAlert();
                this.parseSuccess = false;
                return;
            }
            cellState.put(Arrays.asList(currRow,currCol),currState);
        }
        // Error case: number of cells does not match grid width/height configuration
        if(cellState.keySet().size()!=myWidth*myHeight){
            this.myAlert = XMLAlert.CellInfoAlert;
            this.myAlert.showAlert();
            this.parseSuccess = false;
        }
    }


    public String getSimType(){
        return this.mySimulationType;
    }

    public Element getSimRoot(){
        return this.mySimRoot;
    }

    public String getSimTitle(){
        return this.mySimulationTitle;
    }

    public String getAuthor(){
        return this.myAuthor;
    }

    public HashMap<String, String> getStateImg(){
        return this.stateImage;
    }

    public HashMap<String,Double> getStatePercent(){
        return this.statePercent;
    }

    public ArrayList<Double> getParameters(){
        return this.parameters;
    }

    public Integer getWidth(){
        return this.myWidth;
    }

    public Integer getHeight(){
        return this.myHeight;
    }

    public boolean isParseSuccess(){
        return this.parseSuccess;
    }

    public boolean isSpecConfig(){
        return this.specConfig;
    }

    public HashMap<List<Integer>,String> getCellState(){
        return this.cellState;
    }





    public class XMLException extends RuntimeException {
        // for serialization
        private static final long serialVersionUID = 1L;


        /**
         * Create an exception based on an issue in our code.
         */
        public XMLException (String message, Object ... values) {
            super(String.format(message, values));
        }

        /**
         * Create an exception based on a caught exception with a different message.
         */
        public XMLException (Throwable cause, String message, Object ... values) {
            super(String.format(message, values), cause);
        }

        /**
         * Create an exception based on a caught exception, with no additional message.
         */
        public XMLException (Throwable cause) {
            super(cause);
        }
    }



}
