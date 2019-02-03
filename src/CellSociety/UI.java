package CellSociety;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.HashMap;

public class UI extends Scene {
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 600;
    private static final Paint BACKGROUND_FILL = Color.WHITE;
    private static final int GRID_HEIGHT = 400;
    private static final int GRID_WIDTH = 400;
    private static final int CELL_BUFFER = 2;

    private static final int VBOX_BUFFER_TOP = 45;
    private static final int VBOX_BUFFER_SIDE = 30;
    private static final int VBOX_BUFFER_BUTTON = 25;

    private static final int HBOX_BUFFER_TOP = 50;
    private static final int HBOX_BUFFER_SIDE = 50;
    private static final int HBOX_BUFFER_BUTTON = 50;

    private static final ObservableList<String> SIM_OPTIONS = FXCollections.observableArrayList(
            "Fire",
            "Game of Life",
            "Percolation",
            "Segregation",
            "Wa-Tor World"
    );

    private final int GRID_ROW_NUM;
    private final int GRID_COL_NUM;
    private final int CELL_HEIGHT;
    private final int CELL_WIDTH;

    private Group myRoot;
    private Simulation mySimulation;

    public UI(Group root, int width, int height, Simulation s){
        super(root, WINDOW_HEIGHT, WINDOW_WIDTH, BACKGROUND_FILL);
        this.mySimulation = s;
        myRoot = root;
        GRID_COL_NUM = width;
        GRID_ROW_NUM = height;
        CELL_HEIGHT = GRID_HEIGHT/GRID_ROW_NUM;
        CELL_WIDTH = GRID_WIDTH/GRID_COL_NUM;

        setupButtons();
    }

    public void drawGrid(){
        HashMap<String, String> map = mySimulation.getStateImageMap();
        for (int i = 0; i < GRID_ROW_NUM; i++){
            for (int j = 0; j < GRID_COL_NUM; j++){
                Cell cellData = mySimulation.getGrid()[i][j];
                String cellState = cellData.getState();
                String cellColor = map.get(cellState);
                Rectangle cell = new Rectangle(CELL_WIDTH - CELL_BUFFER, CELL_HEIGHT - CELL_BUFFER);
                cell.setFill(Color.web(cellColor)); //
                cell.setX(j * CELL_WIDTH);
                cell.setY(i * CELL_HEIGHT);
                myRoot.getChildren().add(cell);
                setupButtons();
            }
        }
    }

    private void setupButtons(){
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        borderPane.setRight(addVBox());
        borderPane.setBottom(addHBox());
        myRoot.getChildren().add(borderPane);
    }

    private VBox addVBox(){
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE, VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE));
        vbox.setSpacing(VBOX_BUFFER_BUTTON);
        vbox.setStyle("-fx-background-color: #6ae2c2");
        vbox.getChildren().addAll(
                resetButton(),
                startButton(),
                pauseButton(),
                resumeButton(),
                slowDownButton(),
                speedUpButton(),
                switchSimulationDropdown()
        );
        return vbox;
    }

    private HBox addHBox(){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(HBOX_BUFFER_TOP, HBOX_BUFFER_SIDE, HBOX_BUFFER_TOP, HBOX_BUFFER_SIDE));
        hbox.setSpacing(HBOX_BUFFER_BUTTON);
        hbox.setStyle("-fx-background-color: #84eeff");
        return hbox;
    }

    private Button resetButton(){
        Button resetButton = new Button("Reset");
        resetButton.setOnMouseClicked(e -> mySimulation.resetSimulation());
        return resetButton;
    }

    private Button startButton(){
        Button startButton = new Button("Start");
        startButton.setOnMouseClicked(e -> mySimulation.initTimeline());
        return startButton;
    }

    private Button pauseButton(){
        Button pauseButton = new Button("Pause");
        pauseButton.setOnMouseClicked(e -> mySimulation.pauseSimulation());
        return pauseButton;
    }

    private Button resumeButton(){
        Button stopButton = new Button("Resume");
        stopButton.setOnMouseClicked(e -> mySimulation.resumeSimulation());
        return stopButton;
    }

    private Button slowDownButton(){
        Button slowDownButton = new Button("Slow Down");
        slowDownButton.setOnMouseClicked(e -> mySimulation.slowdown());
        return slowDownButton;
    }

    private Button speedUpButton(){
        Button speedUpButton = new Button("Speed Up");
        speedUpButton.setOnMouseClicked(e -> mySimulation.speedup());
        return speedUpButton;
    }

    private ComboBox switchSimulationDropdown(){
        ComboBox switchSimulationDropdown = new ComboBox(SIM_OPTIONS);
        switchSimulationDropdown.setOnAction(e -> {
            String simulationType = (String) switchSimulationDropdown.getSelectionModel().getSelectedItem();
            mySimulation.switchSimulation(simulationType);
        });
        return switchSimulationDropdown;
    }

}
