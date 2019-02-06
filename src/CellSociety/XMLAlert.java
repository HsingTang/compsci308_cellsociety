package CellSociety;

import javafx.scene.control.Alert;

public enum XMLAlert{
    FileNotFoundAlert("IOException at XMLParser","XML file is not found.","Upload a valid simulation XML file or verify that default XML file exists in resources folder."),
    ParserConfigAlert("Parser Configuration Exception","Something went wrong when initializing XMLParser.","Relaunch the program and try again. Contact the developer if the issue is not resolved."),
    SAXAlert("SAX Exception at XMLParser","XML file is invalid.","Upload a valid simulation XML file."),
    SimTypeAlert("Simulation Model Error","This simulation model is not recognized.","Upload a valid simulation XML configuration file."),
    SimParamAlert("Simulation Parameter Error","Invalid parameters for this simulation model.","Upload a valid simulation XML configuration file."),
    SimStateAlert("Simulation State Error","Invalid state configuration for this simulation model.","Upload a valid simulation XML configuration file."),
    CellLocationAlert("Cell Configuration Error","Cell's location index out of grid bounds.","Upload a valid simulation XML configuration file.");


    private Alert myAlertBox;

    XMLAlert(String title, String header, String content){
        this.myAlertBox = new Alert(Alert.AlertType.ERROR);
        this.myAlertBox.setTitle(title);
        this.myAlertBox.setHeaderText(header);
        this.myAlertBox.setContentText(content);
    }

    public void showAlert(){
        this.myAlertBox.showAndWait();
    }



}
