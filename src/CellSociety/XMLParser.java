package CellSociety;

import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class XMLParser {
    public static final String SIM_TYPE_TAG = "Type";
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
    private String mySimulationType;
    private Element mySimRoot;
    private Integer myWidth;
    private Integer myHeight;
    private HashMap<String, String> stateImage = new HashMap<>();
    private HashMap<String, Double> statePercent = new HashMap<>();
    private ArrayList<Double> parameters = new ArrayList<>();



    public XMLParser(File f){
        this.myFile = f;
        try{
            this.myDBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch (Exception e){
            throw new XMLException(e);
        }
        this.mySimRoot = getRootElement(f);
        this.parseSimConfig();
        this.parseState();
        this.parseParam();
    }

    // Get root element of an XML file
    private Element getRootElement (File xmlFile) {
        try {
            myDBuilder.reset();
            this.myDoc = myDBuilder.parse(xmlFile);
            return this.myDoc.getDocumentElement();
        }
        catch (SAXException | IOException e) {
            throw new XMLException(e);
        }
    }

    private void parseSimConfig(){
        mySimulationType = mySimRoot.getElementsByTagName(SIM_TYPE_TAG).item(0).getTextContent();
        myWidth = Integer.valueOf(mySimRoot.getElementsByTagName(WIDTH_TAG).item(0).getTextContent());
        myHeight = Integer.valueOf(mySimRoot.getElementsByTagName(HEIGHT_TAG).item(0).getTextContent());
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

    public HashMap<String, String> getStateImg(){
        return this.stateImage;
    }

    public HashMap<String,Double> getStatePercent(){
        return this.statePercent;
    }

    public ArrayList<Double> getParameters(){
        return this.parameters;
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