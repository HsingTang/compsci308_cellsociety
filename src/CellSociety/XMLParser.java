package CellSociety;

import javafx.scene.control.Alert;
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
    public static final String STATE_TAG = "State";
    public static final String STATE_NAME_TAG = "StateName";
    public static final String STATE_IMG_TAG = "StateImage";
    public static final String STATE_PERCENT_TAG = "StatePercentage";
    public static final String PARAMETER_TAG = "Parameter";
    private File myFile;
    private DocumentBuilder myDBuilder;
    private Document myDoc;
    private String mySimulationType = "";
    private String mySimulationTitle;
    private String myAuthor;
    private Element mySimRoot;
    private Integer myWidth;
    private Integer myHeight;
    private HashMap<String, String> stateImage = new HashMap<>();
    private HashMap<String, Double> statePercent = new HashMap<>();
    private ArrayList<Double> parameters = new ArrayList<>();
    private XMLAlert myAlert;



    public XMLParser(File f){
        this.myFile = f;
        try{
            this.myDBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch (ParserConfigurationException e){
            this.myAlert = XMLAlert.ParserConfigAlert;
            this.myAlert.showAlert();
            return;
        }
        this.mySimRoot = getRootElement(f);
        if(this.mySimRoot!=null){
            this.parseSimConfig();
            this.parseTitle();
            this.parseAuthor();
            this.parseState();
            this.parseParam();
        }

    }

    // Get root element of an XML file
    private Element getRootElement (File xmlFile) {
        myDBuilder.reset();
        try {
            this.myDoc = myDBuilder.parse(xmlFile);
            return this.myDoc.getDocumentElement();
        }
        catch (SAXException e) {
            this.myAlert = XMLAlert.SAXAlert;
            this.myAlert.showAlert();
        }catch(IOException e){
            this.myAlert = XMLAlert.FileNotFoundAlert;
            this.myAlert.showAlert();
        }
        return null;
    }

    private void parseSimConfig(){
        mySimulationType = this.mySimRoot.getElementsByTagName(SIM_TYPE_TAG).item(0).getTextContent();
        myWidth = Integer.valueOf(this.mySimRoot.getElementsByTagName(WIDTH_TAG).item(0).getTextContent());
        myHeight = Integer.valueOf(this.mySimRoot.getElementsByTagName(HEIGHT_TAG).item(0).getTextContent());
    }


    private void parseTitle(){
        mySimulationTitle = this.mySimRoot.getElementsByTagName(TITLE_TAG).item(0).getTextContent();
    }


    private void parseAuthor(){
        myAuthor = this.mySimRoot.getElementsByTagName(AUTHOR_TAG).item(0).getTextContent();
    }



    private void parseState(){
        NodeList stateList = this.mySimRoot.getElementsByTagName(STATE_TAG);
        for(int i = 0; i<stateList.getLength(); i++){
            Node stateNode = stateList.item(i);
            String currStateName = ((Element)stateNode).getElementsByTagName(STATE_NAME_TAG).item(0).getTextContent();
            stateImage.put(currStateName,((Element)stateNode).getElementsByTagName(STATE_IMG_TAG).item(0).getTextContent());
            statePercent.put(currStateName,Double.valueOf(((Element)stateNode).getElementsByTagName(STATE_PERCENT_TAG).item(0).getTextContent()));
        }
    }

    private void parseParam(){
        NodeList paramList = this.mySimRoot.getElementsByTagName(PARAMETER_TAG);
        for(int i = 0; i<paramList.getLength(); i++){
            Node paramNode = paramList.item(i);
            parameters.add(Double.valueOf(paramNode.getTextContent()));
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
