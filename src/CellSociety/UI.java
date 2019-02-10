package CellSociety;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;

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

    private final int GRID_ROW_NUM;
    private final int GRID_COL_NUM;
    private final int CELL_HEIGHT;
    private final int CELL_WIDTH;
    private final ObservableList<String> SIM_OPTIONS;

    private ResourceBundle myResources;

    private Group myRoot;
    private Simulation mySimulation;
    private Map<Cell, Polygon> cellVisMap;
    private Integer[] myStartingCoordinates;
    private Map<String, String> stateMap;
    private Map<String, XYChart.Series> stateSeriesMap;
    private int stepNum;

    public UI(Group root, int width, int height, String cellShape, Simulation s){
        super(root, WINDOW_HEIGHT, WINDOW_WIDTH, BACKGROUND_FILL);
        this.mySimulation = s;
        myRoot = root;
        GRID_COL_NUM = width;
        GRID_ROW_NUM = height;
        CELL_HEIGHT = GRID_HEIGHT/GRID_ROW_NUM;
        CELL_WIDTH = GRID_WIDTH/GRID_COL_NUM;
        myResources = ResourceBundle.getBundle("/resources/English");
        SIM_OPTIONS = FXCollections.observableArrayList(
                myResources.getString("Fire"),
                myResources.getString("GOL"),
                myResources.getString("Perc"),
                myResources.getString("Seg"),
                myResources.getString("WaTor"));
        initStartingCoordinates(cellShape);
        stepNum = 0;
        initCellVisMap();
        setupLayout();
    }

    public void drawGrid(){
        for (Cell key: cellVisMap.keySet()){
            String stateVis = stateMap.get(key.getState());
            cellVisMap.get(key).setFill(Color.web(stateVis));
        }
    }

    public void drawGraph(){
        for (Map.Entry<String, XYChart.Series> stateSeries: stateSeriesMap.entrySet()){
            double statePercent = calcCellStatePercentage(stateSeries.getKey());
            stateSeries.getValue().getData().add(new XYChart.Data(stepNum, statePercent));
        }
        stepNum++;
    }

    private void initStartingCoordinates(String shape){
        switch (shape){
            case "Rectangle":
                myStartingCoordinates = new Integer[]{
                        0, 0,
                        CELL_WIDTH, 0,
                        0, CELL_HEIGHT,
                        CELL_WIDTH, CELL_HEIGHT};
                break;
            case "Triangle":
                myStartingCoordinates = new Integer[]{
                        CELL_WIDTH/2, 0,
                        0, CELL_HEIGHT,
                        CELL_WIDTH, CELL_HEIGHT};
                break;
        }
    }
    private LineChart<Number, Number> addGraph(){
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis,yAxis);
        xAxis.setLabel(myResources.getString("XAxisLabel"));
        yAxis.setLabel(myResources.getString("YAxisLabel"));
        lineChart.setTitle(myResources.getString("LineChartTitle"));
        initStateSeriesMap(lineChart);
        return lineChart;
    }

    private void initStateSeriesMap(LineChart lineChart){
        stateSeriesMap = new HashMap<>();
        for (String state: stateMap.keySet()){
            XYChart.Series series = new XYChart.Series();
            series.setName(state);
            stateSeriesMap.put(state, series);
            lineChart.getData().add(series);
        }
    }

    private HashMap<String, Integer> makeStateNumMap(){
        HashMap<String, Integer> stateNumMap = new HashMap<>();
        for (String state: stateMap.keySet()){
            stateNumMap.putIfAbsent(state, 0);
        }
        for (Cell cell: cellVisMap.keySet()){
            stateNumMap.put(cell.getState(), stateNumMap.get(cell.getState()) + 1);
        }
        return stateNumMap;
    }

    private double calcCellStatePercentage(String state){
        int numCells = makeStateNumMap().get(state);
        double percent = numCells/(GRID_COL_NUM * GRID_ROW_NUM);
        return percent;
    }

    private void initCellVisMap(){
        stateMap = mySimulation.getStateImageMap();
        Cell[][] cells = mySimulation.getGrid();
        cellVisMap = new HashMap<>();
        for (int i = 0; i < GRID_ROW_NUM; i++){
            for (int j = 0; j < GRID_COL_NUM; j++){
                Cell cell = cells[i][j];
                Polygon cellShape = new Polygon(calcCurrentCoordinates(i, j));
                cellShape.setFill(Color.web(stateMap.get(cell.getState())));
                cellVisMap.put(cell, cellShape);
            }
        }
        for (Cell key: cellVisMap.keySet()){
            myRoot.getChildren().add(cellVisMap.get(key));
        }
    }

    private double[] calcCurrentCoordinates(int row, int col){
        double [] currentCoordinates = new double[myStartingCoordinates.length];
        for (int i = 0; i < myStartingCoordinates.length; i++){
            if (i % 2 == 0){
                currentCoordinates[i] = myStartingCoordinates[i] + col * CELL_WIDTH + CELL_BUFFER;
            }
            else{
                currentCoordinates[i] = myStartingCoordinates[i] + row * CELL_WIDTH + CELL_BUFFER;
            }
        }
        return currentCoordinates;
    }

    private void setupLayout(){
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
                //slowDownButton(),
                //speedUpButton(),
                speedLabel(),
                speedSlider(),
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
        hbox.getChildren().add(addGraph());
        return hbox;
    }


    private Button resetButton(){
        Button resetButton = new Button(myResources.getString("ResetButton"));
        resetButton.setOnMouseClicked(e -> mySimulation.resetSimulation());
        return resetButton;
    }

    private Button startButton(){
        Button startButton = new Button(myResources.getString("StartButton"));
        startButton.setOnMouseClicked(e -> mySimulation.playSimulation());
        return startButton;
    }

    private Button pauseButton(){
        Button pauseButton = new Button(myResources.getString("PauseButton"));
        pauseButton.setOnMouseClicked(e -> mySimulation.pauseSimulation());
        return pauseButton;
    }

    private Button resumeButton(){
        Button stopButton = new Button(myResources.getString("ResumeButton"));
        stopButton.setOnMouseClicked(e -> mySimulation.playSimulation());
        return stopButton;
    }

    /*private Button slowDownButton(){
        Button slowDownButton = new Button(myResources.getString("SlowDownButton"));
        slowDownButton.setOnMouseClicked(e -> mySimulation.slowdown());
        return slowDownButton;
    }

    private Button speedUpButton(){
        Button speedUpButton = new Button(myResources.getString("SpeedUpButton"));
        speedUpButton.setOnMouseClicked(e -> mySimulation.speedup());
        return speedUpButton;
    }*/

    private Slider speedSlider(){
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(50);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(10);
        slider.valueProperty().addListener(e -> mySimulation.setSpeed(slider.getValue()));
        return slider;
    }

    private Label speedLabel(){
        Label speed = new Label(myResources.getString("SpeedSlider"));
        return speed;
    }

    private Button stepButton(){
        Button stepButton = new Button(myResources.getString("StepButton"));
        stepButton.setOnMouseClicked(e -> mySimulation.stepSimulation());
        return stepButton;
    }

    private ComboBox switchSimulationDropdown(){
        ComboBox switchSimulationDropdown = new ComboBox(SIM_OPTIONS);
        switchSimulationDropdown.setOnAction(e -> {
            String simulationType = (String) switchSimulationDropdown.getSelectionModel().getSelectedItem();
            String simFileName = simulationType;
            mySimulation.switchSimulation(simFileName);
        });
        return switchSimulationDropdown;
    }
}
