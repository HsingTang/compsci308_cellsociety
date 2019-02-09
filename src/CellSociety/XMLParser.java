package CellSociety;


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
    private final String ALERT_CONFIG_PATH = "resources/XMLAlertText.txt";
    private final String SIM_TYPE_TAG = "Type";
    private final String TITLE_TAG = "Title";
    private final String AUTHOR_TAG = "Author";
    private final String WIDTH_TAG = "Width";
    private final String HEIGHT_TAG = "Height";
    private final String CONFIG_TAG = "SpecifiedConfig";
    private final String STATE_TAG = "State";
    private final String STATE_NAME_TAG = "StateName";
    private final String STATE_IMG_TAG = "StateImage";
    private final String STATE_PERCENT_TAG = "StatePercentage";
    private final String PARAMETER_TAG = "Parameter";
    private final String CELL_TAG = "Cell";
    private final String CELL_ROW_TAG = "Row";
    private final String CELL_COL_TAG = "Col";
    private final String CELL_STATE_TAG = "CellState";
    // private File myFile;
    XMLAlert fileNotFoundAlert = new XMLAlert();
    XMLAlert parserConfigAlert = new XMLAlert();
    XMLAlert SAXAlert = new XMLAlert();
    XMLAlert gridErrAlert = new XMLAlert();
    XMLAlert modelErrAlert = new XMLAlert();
    XMLAlert paramErrAlert = new XMLAlert();
    XMLAlert configErrAlert = new XMLAlert();
    XMLAlert stateErrAlert = new XMLAlert();
    XMLAlert cellIdxAlert = new XMLAlert();
    XMLAlert cellStateAlert = new XMLAlert();
    XMLAlert cellConfigAlert = new XMLAlert();
    XMLAlert cellInfoAlert = new XMLAlert();
    final XMLAlert[] myAlertArr = new XMLAlert[]{fileNotFoundAlert, parserConfigAlert, SAXAlert,
            gridErrAlert,modelErrAlert, paramErrAlert, configErrAlert, stateErrAlert, cellIdxAlert, cellStateAlert,
            cellConfigAlert, cellInfoAlert};


    private DocumentBuilder myDBuilder;
    private String mySimulationType = "";
    private String mySimulationTitle = "";
    private String myAuthor = "";
    private Element mySimRoot;
    private Integer myWidth;
    private Integer myHeight;
    private HashMap<String, String> stateImage = new HashMap<>();
    private HashMap<String, Double> statePercent = new HashMap<>();
    private HashMap<List<Integer>, String> cellState = new HashMap<>();
    private ArrayList<Double> parameters = new ArrayList<>();
    private boolean specConfig = false;
    private boolean parseSuccess = true;



    public XMLParser(File f) throws Exception {
        try {
            setupAlert();
        } catch (Exception e) {
            throw new Exception("Alert Setup went wrong", e);
        }
        try {
            this.myDBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // Error case: ParserConfigurationException handling
            throw new Exception("ParserConfigurationException occurs",e);
        }
        try{
            this.mySimRoot = getRootElement(f);
        }catch (SAXException e){
            callAlert(SAXAlert);
        }catch (IOException e){
            callAlert(fileNotFoundAlert);
        }

        if (this.mySimRoot != null) {
            this.parseSimConfig();
            this.parseSpecConfig();
            this.parseTitle();
            this.parseAuthor();
            this.parseState();
            this.parseParam();
            this.parseCell();
        }

    }


    private void setupAlert() throws IOException {
        Scanner sc = new Scanner(new File(ALERT_CONFIG_PATH));
        int idx = 0;
        while (sc.hasNextLine()&&idx<myAlertArr.length) {
            String[] alertText = sc.nextLine().split(";");
            myAlertArr[idx].setText(alertText[0],alertText[1],alertText[2]);
            idx++;
        }
    }


    private void callAlert(XMLAlert a){
        a.showAlert();
        this.parseSuccess = false;
    }


    // Get root element of an XML file
    private Element getRootElement(File xmlFile) throws IOException,SAXException {
        Document myDoc;
        myDBuilder.reset();
        try {
            myDoc = myDBuilder.parse(xmlFile);
            return myDoc.getDocumentElement();
        }
        // Error case: exception handling
        catch (SAXException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }


    private void parseSimConfig() {
        NodeList simTypeNode = this.mySimRoot.getElementsByTagName(SIM_TYPE_TAG);
        // Error case: missing simulation type information
        if (simTypeNode.getLength() == 0) {
            callAlert(modelErrAlert);
            return;
        }
        NodeList widthNode = this.mySimRoot.getElementsByTagName(WIDTH_TAG);
        NodeList heightNode = this.mySimRoot.getElementsByTagName(HEIGHT_TAG);
        // Error case: missing simulation grid size information
        if (widthNode.getLength() == 0 || heightNode.getLength() == 0) {
            callAlert(gridErrAlert);
            return;
        }
        mySimulationType = simTypeNode.item(0).getTextContent();
        myWidth = Integer.valueOf(widthNode.item(0).getTextContent());
        myHeight = Integer.valueOf(heightNode.item(0).getTextContent());
    }


    private void parseTitle() {
        NodeList titleNode = this.mySimRoot.getElementsByTagName(TITLE_TAG);
        if (titleNode.getLength() != 0) {
            mySimulationTitle = titleNode.item(0).getTextContent();
        }
    }


    private void parseAuthor() {
        NodeList authorNode = this.mySimRoot.getElementsByTagName(AUTHOR_TAG);
        if (authorNode.getLength() != 0) {
            myAuthor = authorNode.item(0).getTextContent();
        }
    }


    private void parseSpecConfig() {
        NodeList specNode = this.mySimRoot.getElementsByTagName(CONFIG_TAG);
        // Error case: Missing file parsing specification info
        if (specNode.getLength() == 0) {
            callAlert(configErrAlert);
            return;
        }
        if (specNode.item(0).getTextContent().equals("true")) {
            this.specConfig = true;
        } else {
            this.specConfig = false;
        }
    }


    private void parseState() {
        NodeList stateList = this.mySimRoot.getElementsByTagName(STATE_TAG);
        // Error case: missing state information
        if (stateList.getLength() == 0) {
            callAlert(stateErrAlert);
            return;
        }
        for (int i = 0; i < stateList.getLength(); i++) {
            Node stateNode = stateList.item(i);
            NodeList currName = ((Element) stateNode).getElementsByTagName(STATE_NAME_TAG);
            NodeList currImg = ((Element) stateNode).getElementsByTagName(STATE_IMG_TAG);
            // Error case: missing image for the specified state
            if (currImg.getLength() == 0 || currName.getLength() == 0) {
                callAlert(stateErrAlert);
                return;
            }
            String currStateName = currName.item(0).getTextContent();
            stateImage.put(currStateName, currImg.item(0).getTextContent());

            NodeList currPercent = ((Element) stateNode).getElementsByTagName(STATE_PERCENT_TAG);
            if (currPercent.getLength() != 0) {
                statePercent.put(currStateName, Double.valueOf(currPercent.item(0).getTextContent()));
            }
        }
        // Error case: number of states does not match state percentage map size
        if (stateImage.keySet().size() != statePercent.keySet().size() && statePercent.keySet().size() != 0) {
            callAlert(stateErrAlert);
        }
    }


    private void parseParam() {
        NodeList paramList = this.mySimRoot.getElementsByTagName(PARAMETER_TAG);
        for (int i = 0; i < paramList.getLength(); i++) {
            Node paramNode = paramList.item(i);
            parameters.add(Double.valueOf(paramNode.getTextContent()));
        }
    }


    private void parseCell() {
        NodeList cellList = this.mySimRoot.getElementsByTagName(CELL_TAG);
        // Error case: file parsing specification does not match cell info
        if ((cellList.getLength() != 0 && !this.specConfig) || (cellList.getLength() == 0 && this.specConfig)) {
            callAlert(cellConfigAlert);
            return;
        } else if (!this.specConfig) {
            return;
        }
        for (int i = 0; i < cellList.getLength(); i++) {
            Node cellNode = cellList.item(i);
            // Error case: missing cell information
            if (((Element) cellNode).getElementsByTagName(CELL_ROW_TAG).getLength() == 0
                    || (((Element) cellNode).getElementsByTagName(CELL_COL_TAG).getLength() == 0)
                    || ((Element) cellNode).getElementsByTagName(CELL_STATE_TAG).getLength() == 0) {
                callAlert(cellInfoAlert);
                return;
            }
            int currRow = Integer.valueOf(((Element) cellNode).getElementsByTagName(CELL_ROW_TAG).item(0).getTextContent());
            int currCol = Integer.valueOf(((Element) cellNode).getElementsByTagName(CELL_COL_TAG).item(0).getTextContent());
            // Error case: cell index out of bounds
            if (currRow < 0 || currCol < 0 || currRow >= myHeight || currCol >= myWidth) {
                callAlert(cellIdxAlert);
                return;
            }
            String currState = ((Element) cellNode).getElementsByTagName(CELL_STATE_TAG).item(0).getTextContent();
            // Error case: invalid cell state configuration
            if (!this.stateImage.containsKey(currState)) {
                callAlert(cellStateAlert);
                return;
            }
            cellState.put(Arrays.asList(currRow, currCol), currState);
        }
        // Error case: number of cells does not match grid width/height configuration
        if (cellState.keySet().size() != myWidth * myHeight) {
            callAlert(cellInfoAlert);
        }
    }


    public String getSimType() {
        return this.mySimulationType;
    }

    public Element getSimRoot() {
        return this.mySimRoot;
    }

    public String getSimTitle() {
        return this.mySimulationTitle;
    }

    public String getAuthor() {
        return this.myAuthor;
    }

    public Map<String, String> getStateImg() {
        return Collections.unmodifiableMap(this.stateImage);
    }

    public Map<String, Double> getStatePercent() {
        return Collections.unmodifiableMap(this.statePercent);
    }

    public List<Double> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    public Integer getWidth() {
        return this.myWidth;
    }

    public Integer getHeight() {
        return this.myHeight;
    }

    public boolean isParseSuccess() {
        return this.parseSuccess;
    }

    public boolean isSpecConfig() {
        return this.specConfig;
    }

    public Map<List<Integer>, String> getCellState() {
        return this.cellState;
    }


//    public class XMLException extends RuntimeException {
//        // for serialization
//        private static final long serialVersionUID = 1L;
//
//
//        /**
//         * Create an exception based on an issue in our code.
//         */
//        public XMLException (String message, Object ... values) {
//            super(String.format(message, values));
//        }
//
//        /**
//         * Create an exception based on a caught exception with a different message.
//         */
//        public XMLException (Throwable cause, String message, Object ... values) {
//            super(String.format(message, values), cause);
//        }
//
//        /**
//         * Create an exception based on a caught exception, with no additional message.
//         */
//        public XMLException (Throwable cause) {
//            super(cause);
//        }
//    }


}
