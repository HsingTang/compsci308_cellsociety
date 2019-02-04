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
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.HashMap;

public class UI extends Scene {
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 600;
    private static final Paint BACKGROUND_FILL = Color.WHITE;
    private static final int GRID_HEIGHT = 400;
    private static final int GRID_WIDTH = 400;
    private static final int CELL_BUFFER = 1;

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
            "WaTor World"
    );

    private final int GRID_ROW_NUM;
    private final int GRID_COL_NUM;
    private final int CELL_HEIGHT;
    private final int CELL_WIDTH;

    private Group myRoot;
    private Simulation mySimulation;
    private HashMap<Cell, Rectangle> cellVisMap;
    HashMap<String, String> stateMap;


    public UI(Group root, int width, int height, Simulation s){
        super(root, WINDOW_HEIGHT, WINDOW_WIDTH, BACKGROUND_FILL);
        this.mySimulation = s;
        myRoot = root;
        GRID_COL_NUM = width;
        GRID_ROW_NUM = height;
        CELL_HEIGHT = GRID_HEIGHT/GRID_ROW_NUM;
        CELL_WIDTH = GRID_WIDTH/GRID_COL_NUM;
        initCellVisMap();
        setupButtons();
    }

    public void drawGrid(){
        for (Cell key: cellVisMap.keySet()){
            String stateVis = stateMap.get(key.getState());
            cellVisMap.get(key).setFill(Color.web(stateVis));
        }
    }

    private void initCellVisMap(){
        stateMap = mySimulation.getStateImageMap();
        Cell[][] cells = mySimulation.getGrid();
        cellVisMap = new HashMap<>();
        for (int i = 0; i < GRID_ROW_NUM; i++){
            for (int j = 0; j < GRID_COL_NUM; j++){
                Cell cell = cells[i][j];
                Rectangle cellRect = new Rectangle(CELL_WIDTH - CELL_BUFFER, CELL_HEIGHT - CELL_BUFFER);
                cellRect.setFill(Color.web(stateMap.get(cell.getState())));
                cellRect.setX(j * CELL_WIDTH);
                cellRect.setY(i * CELL_HEIGHT);
                cellVisMap.put(cell, cellRect);
            }
        }
        for (Cell key: cellVisMap.keySet()){
            myRoot.getChildren().add(cellVisMap.get(key));
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
                stepButton(),
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
        startButton.setOnMouseClicked(e -> mySimulation.playSimulation());
        return startButton;
    }

    private Button pauseButton(){
        Button pauseButton = new Button("Pause");
        pauseButton.setOnMouseClicked(e -> mySimulation.pauseSimulation());
        return pauseButton;
    }

    private Button resumeButton(){
        Button stopButton = new Button("Resume");
        stopButton.setOnMouseClicked(e -> mySimulation.playSimulation());
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

    private Button stepButton(){
        Button stepButton = new Button("Step");
        stepButton.setOnMouseClicked(e -> mySimulation.stepSimulation());
        return stepButton;
    }

    private ComboBox switchSimulationDropdown(){
        ComboBox switchSimulationDropdown = new ComboBox(SIM_OPTIONS);
        switchSimulationDropdown.setOnAction(e -> {
            String simulationType = (String) switchSimulationDropdown.getSelectionModel().getSelectedItem();
            String simFileName = "resources/" + simulationType + ".xml";
            mySimulation.switchSimulation(simFileName);
            mySimulation.initGrid();
        });
        return switchSimulationDropdown;
    }
}
