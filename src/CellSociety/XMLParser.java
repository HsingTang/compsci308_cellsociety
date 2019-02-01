package CellSociety;

import javafx.scene.*;
import javafx.scene.image.ImageView;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class XMLParser {
    private File myFile;
    private DocumentBuilder myDBuilder = null;
    private String mySimulationType;
    private Element myFileRoot;
    private Element myStateRoot;
    public HashMap<String, ImageView> stateImage = new HashMap<>();
    public HashMap<String, Double> statePercent = new HashMap<>();
    public static final List<String> FILE_DATA_FIELDS = List.of(
            "Simulation Type",
            "State",
            "rating",
            "year"
    );


    public XMLParser(File f){
        this.myFile = f;
        try{
            this.myDBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getStage(Element root){

    }



}
