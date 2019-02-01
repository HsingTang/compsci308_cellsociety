package CellSociety;

import javafx.scene.*;
import javafx.scene.image.ImageView;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class XMLParser {
    public static final String SIM_TAG = "Simulation Type";
    public static final String STATE_TAG = "State";
    public static final String STATE_NAME_TAG = "State Name";
    public static final String STATE_IMG_TAG = "State Image";
    public static final String STATE_PERCENT_TAG = "State Percentage";
    public static final String PARAMETER_TAG = "Parameters";
    private File myFile;
    private DocumentBuilder myDBuilder;
    private Document myDoc;
    private String mySimulationType;
    private Element myFileRoot;
    public HashMap<String, String> stateImage = new HashMap<>();
    public HashMap<String, Double> statePercent = new HashMap<>();
    public ArrayList<Double> parameters = new ArrayList<>();



    public XMLParser(File f){
        this.myFile = f;
        try{
            this.myDBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch (Exception e){
            throw new XMLException(e);
        }
        this.myFileRoot = getRootElement(f);

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

    private void parseSimType(){
        this.mySimulationType = this.myFileRoot.getElementsByTagName(SIM_TAG).item(0).getTextContent();
    }

    private void parseState(){
        NodeList stateList = this.myDoc.getElementsByTagName(STATE_TAG);
        for(int i = 0; i<stateList.getLength(); i++){
            Node stateNode = stateList.item(i);
            this.stateImage.put(((Element)stateNode).getElementsByTagName(STATE_NAME_TAG).item(0).getTextContent(),((Element)stateNode).getElementsByTagName(STATE_IMG_TAG).item(0).getTextContent());
            this.statePercent.put(((Element)stateNode).getElementsByTagName(STATE_NAME_TAG).item(0).getTextContent(),Double.valueOf(((Element)stateNode).getElementsByTagName(STATE_PERCENT_TAG).item(0).getTextContent()));
        }
    }

    private void parseParam(){
        NodeList paramList = this.myDoc.getElementsByTagName(PARAMETER_TAG);
        for(int i = 0; i<paramList.getLength(); i++){
            Node paramNode = paramList.item(i);
            this.parameters.add(Double.valueOf(paramNode.getTextContent()));
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
