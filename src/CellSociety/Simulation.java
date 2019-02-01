package CellSociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.ceil;

public class Simulation extends Application{

    public static final int DEFAULT_WIDTH = 20;
    public static final int DEFAULT_HEIGHT = 20;
    public static final String GOL_XML = "Game of Life.xml";
    public static final String WATOR_XML = "WaTor.xml";
    public static final String FIRE_XML = "Fire.xml";
    public static final String SEG_XML = "Segregation.xml";
    public static final String PERC_XML = "Percolation.xml";

    private Timeline myTimeline;
    private Stage myStage;
    private double delay = 1000;
    private Cell[][] myGrid;
    private Scene myIntroScene;
    private UI myUIScene;
    private Group myIntroRoot;
    private Group myUIRoot;
    private int myWidth = DEFAULT_WIDTH;
    private int myHeight = DEFAULT_HEIGHT;
    private String SIM_TYPE;
    private boolean pause = false;
    private boolean step = true;
    private XMLParser myParser;
    private HashMap<String, String> stateImageMap;
    private HashMap<String, Double> statePercentMap;
    private ArrayList<Double> parametersList;
    private ArrayList<String> stateList;




    public void start (Stage stage) {
        this.myStage = stage;
        initIntroScene();
        // somewhere in the scene's method of handling button,
        // when a button is pressed, SIM_TYPE will be updated to corresponding file name
        readXML();
        myUIScene = new UI(myIntroRoot,DEFAULT_WIDTH,DEFAULT_HEIGHT,this);
    }


    private void initIntroScene(){
        myIntroRoot = new Group();
        myIntroScene = new IntroScene(myIntroRoot,DEFAULT_WIDTH,DEFAULT_HEIGHT,this);
        myStage.setScene(myIntroScene);
        myStage.setTitle("Cell Society");
        myStage.show();
    }


    public void setSimType(String s){
        this.SIM_TYPE = s;
    }


    private void initStateList(){
        stateList = new ArrayList<>();
        for(String state:statePercentMap.keySet()){
            for(int i = 0; i<ceil(statePercentMap.get(state)*10); i++){
                stateList.add(state);
            }
        }
    }

    public void initGrid(){
        readXML();
        initStateList();
        myGrid = new Cell[myHeight][myWidth];
        Random myRandom = new Random();
        for(int i = 0; i<myGrid.length; i++){
            for(int j = 0; j<myGrid[0].length; j++){
                Cell newCell = null;
                int randIdx = myRandom.nextInt(stateList.size());
                switch (SIM_TYPE){
                    case GOL_XML:
                        newCell = new GOLCell(i,j,stateList.get(randIdx),parametersList);
                        break;
                    case WATOR_XML:
                        newCell = new WATORCell(i,j,stateList.get(randIdx),parametersList);
                        break;
                    case FIRE_XML:
                        newCell = new FireCell(i,j,stateList.get(randIdx),parametersList);
                        break;
                    case SEG_XML:
                        newCell = new SegCell(i,j,stateList.get(randIdx),parametersList);
                        break;
                    case PERC_XML:
                        newCell = new PercCell(i,j,stateList.get(randIdx),parametersList);
                        break;
                }
                myGrid[i][j] = newCell;
            }
        }
        for(int i = 0; i<myGrid.length; i++){
            for (int j = 0; j<myGrid[0].length; j++){
                myGrid[i][j].findNeighbors(myGrid);
            }
        }
        initUI();
    }


    private void initUI(){
        myUIRoot = new Group();
        myUIScene = new UI(myUIRoot, myWidth, myHeight, this);
        myStage.setScene(myUIScene);
        initTimeline();
    }


    private void initTimeline(){
        var frame = new KeyFrame(Duration.millis(delay),e->updateGrid());
        this.myTimeline = new Timeline();
        this.myTimeline.setCycleCount(Timeline.INDEFINITE);
        this.myTimeline.getKeyFrames().add(frame);
        this.myTimeline.play();
    }


    private void readXML(){
        File f = new File("resources/"+SIM_TYPE);
        myParser = new XMLParser(f);
        assert ((myParser.getSimType()+".xml").equals(SIM_TYPE));
        this.stateImageMap = myParser.getStateImg();
        this.statePercentMap = myParser.getStatePercent();
        this.parametersList = myParser.getParameters();
        this.myWidth = myParser.getWidth();
        this.myHeight = myParser.getHeight();
    }


    public Cell[][] getGrid(){
        return this.myGrid;
    }


    private void updateGrid(){
        if(!this.pause) {
            for (int i = 0; i < myGrid.length; i++) {
                for (int j = 0; j < myGrid[0].length; j++) {
                    Cell currCell = myGrid[i][j];
                    currCell.findNextState();
                }
            }
            for (int i = 0; i < myGrid.length; i++) {
                for (int j = 0; j < myGrid[0].length; j++) {
                    Cell currCell = myGrid[i][j];
                    currCell.updateState();
                }
            }
        }
    }


    public void pauseSimulation(){
        this.myTimeline.pause();
    }


    public void resumeSimulation(){
        this.myTimeline.play();
    }


    public void stepSimulation(){
        this.myTimeline.pause();
        updateGrid();
    }


    public void resetSimulation(){
        initGrid();
    }


    public void switchSimulation(String newSimType){
        this.setSimType(newSimType);
        initGrid();
    }


    public void slowdown(){
        this.myTimeline.stop();
        this.delay*=2;
        initTimeline();
    }


    public void speedup(){
        this.myTimeline.stop();
        this.delay/=2;
        initTimeline();
    }


    public String getSimulationType(){
        return this.SIM_TYPE;
    }





    /**
     * Main method to launch the Breakout game program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
