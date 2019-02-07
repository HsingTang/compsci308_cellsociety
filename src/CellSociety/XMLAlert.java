package CellSociety;

import javafx.scene.control.Alert;

/**
 * Trigger Alert boxes when XMLParser encounters error/exceptional cases
 * or if any information is missing or mismatched in the parsed XML configuration file
 */
public enum XMLAlert{
    FileNotFoundAlert("IOException at XMLParser","XML file is not found.","Upload a valid simulation XML file or verify that default XML file exists in resources folder."),
    ParserConfigAlert("Parser Configuration Exception","Something went wrong when initializing XMLParser.","Relaunch the program and try again. Contact the developer if the issue is not resolved."),
    SAXAlert("SAX Exception at XMLParser","XML file is invalid.","Upload a valid simulation XML file."),
    GridSizeAlert("Model Grid Configuration Error","Grid width/height not specified.", "Upload a valid simulation XML configuration file."),
    SimTypeAlert("Simulation Model Error","This simulation model type is not specified or invalid.","Upload a valid simulation XML configuration file."),
    SimParamAlert("Simulation Parameter Error","Invalid parameters for this simulation model.","Upload a valid simulation XML configuration file."),
    SpecConfigAlert("Simulation Configuration Error","Configuration file parsing pattern not specified or invalid","Upload a valid simulation XML configuration file."),
    SimStateAlert("Simulation State Error","Invalid state configuration for this simulation model.","Upload a valid simulation XML configuration file."),
    CellLocationAlert("Cell Configuration Error","Cell's location index out of grid bounds.","Upload a valid simulation XML configuration file."),
    CellStateAlert("Cell Configuration Error","Specified cell initial state is invalid.","Upload a valid simulation XML configuration file."),
    CellConfigConflictAlert("Cell Configuration Error","Configuration file parsing pattern conflicts with file content.","Upload a valid simulation XML configuration file."),
    CellInfoAlert("Cell Configuration Error","Missing cell information provided in configuration file","Upload a valid simulation XML configuration file");


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
