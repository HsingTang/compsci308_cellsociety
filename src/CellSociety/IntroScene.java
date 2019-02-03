package CellSociety;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

import java.io.File;

public class IntroScene extends Scene {
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 600;
    private static final int BUTTONS_VBUFFER = 130;
    private static final int VBOX_BUFFER_TOP = 100;
    private static final int VBOX_BUFFER_SIDE = 100;

    private Simulation mySimulation;
    private Group myRoot;

    public IntroScene(Group root, double width, double height, Simulation s){ //width and height params unused
        super(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        myRoot = root;
        this.mySimulation = s;
        setupButtons();
    }

    private void setupButtons(){
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        borderPane.setStyle("-fx-background-color: #a3beff");
        borderPane.setLeft(addVBoxLeft());
        borderPane.setRight(addVBoxRight());
        myRoot.getChildren().add(borderPane);
    }

    private VBox addVBoxLeft(){
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #b5f7ff");
        vbox.setPadding(new Insets(VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE, VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE));

        vbox.setSpacing(BUTTONS_VBUFFER);
        vbox.getChildren().addAll(fireSimButton(), GOLSimButton(), PercSimButton());
        return vbox;
    }

    private VBox addVBoxRight(){
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #f3aaff");
        vbox.setPadding(new Insets(VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE, VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE));
        vbox.setSpacing(BUTTONS_VBUFFER);
        vbox.getChildren().addAll(SegSimButton(), WaTorSimButton(), uploadXML());
        return vbox;
    }

    private Button fireSimButton(){
        Button fireSimButton = new Button("Fire");
        fireSimButton.setOnMouseClicked(e -> mySimulation.setSimType(mySimulation.FIRE_XML));
        return fireSimButton;
    }

    private Button GOLSimButton(){
        Button GOLSimButton = new Button("Game of Life");
        GOLSimButton.setOnMouseClicked(e -> mySimulation.switchSimulation(mySimulation.GOL_XML));
        return GOLSimButton;
    }

    private Button PercSimButton(){
        Button PercSimButton = new Button("Percolation");
        PercSimButton.setOnMouseClicked(e -> mySimulation.switchSimulation(mySimulation.PERC_XML));
        return PercSimButton;
    }

    private Button SegSimButton(){
        Button SegSimButton = new Button("Segregation");
        SegSimButton.setOnMouseClicked(e -> mySimulation.switchSimulation(mySimulation.SEG_XML));
        return SegSimButton;
    }

    private Button WaTorSimButton(){
        Button WaTorSimButton = new Button("Wa-Tor World");
        WaTorSimButton.setOnMouseClicked(e -> mySimulation.switchSimulation(mySimulation.WATOR_XML));
        return WaTorSimButton;
    }

    private Button uploadXML(){
        Button uploadXML = new Button("Upload XML File");
        uploadXML.setOnMouseClicked(e -> chooseFile());
        return uploadXML;
    }

    private void chooseFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        File file = fileChooser.showOpenDialog(new PopupWindow() {
            @Override
            public void show(Window window) {
                super.show(window);
            }
        });
    }
}
