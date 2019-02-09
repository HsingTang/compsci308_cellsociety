package CellSociety;

import javafx.scene.control.Alert;

/**
 * Trigger Alert boxes when XMLParser encounters error/exceptional cases
 * or if any information is missing or mismatched in the parsed XML configuration file
 */
public class XMLAlert{
    private Alert myAlertBox;

    public XMLAlert(String title, String header, String content){
        this.myAlertBox = new Alert(Alert.AlertType.ERROR);
        this.myAlertBox.setTitle(title);
        this.myAlertBox.setHeaderText(header);
        this.myAlertBox.setContentText(content);
    }

    public XMLAlert(){
        this.myAlertBox = new Alert(Alert.AlertType.ERROR);
    }


    public void setText(String t, String h, String c){
        this.myAlertBox.setTitle(t);
        this.myAlertBox.setHeaderText(h);
        this.myAlertBox.setContentText(c);
    }

    public void showAlert(){
        this.myAlertBox.showAndWait();
    }





}
