package CellSociety;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class UI extends Scene {
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 600;
    private static final Paint BACKGROUND_FILL = Color.WHITE;
    private static final int GRID_HEIGHT = 400;
    private static final int GRID_WIDTH = 400;
    private static final int CELL_BUFFER = 2;

    private static final int VBOX_BUFFER_TOP = 25;
    private static final int VBOX_BUFFER_SIDE = 25;
    private static final int VBOX_BUFFER_BUTTON = 15;


    private final int GRID_ROW_NUM;
    private final int GRID_COL_NUM;
    private final int CELL_HEIGHT;
    private final int CELL_WIDTH;

    private Group myRoot;
    private Simulation mySimulation;

    public UI(Group root, int width, int height, Simulation s){
        super(root, WINDOW_HEIGHT, WINDOW_WIDTH, BACKGROUND_FILL);
        mySimulation = s;
        myRoot = root;
        GRID_COL_NUM = width;
        GRID_ROW_NUM = height;
        CELL_HEIGHT = GRID_HEIGHT/GRID_ROW_NUM;
        CELL_WIDTH = GRID_WIDTH/GRID_COL_NUM;
        setupButtons();
    }

    public void drawGrid(){
        for (int i = 0; i < GRID_ROW_NUM; i++){
            for (int j = 0; j < GRID_COL_NUM; j++){
                Rectangle cell = new Rectangle(CELL_WIDTH - CELL_BUFFER, CELL_HEIGHT - CELL_BUFFER);
                cell.setFill(Color.GRAY);
                cell.setX(j * CELL_WIDTH);
                cell.setY(i * CELL_HEIGHT);
                myRoot.getChildren().add(cell);
            }
        }
    }

    private void setupButtons(){
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_HEIGHT, WINDOW_WIDTH);
        borderPane.setRight(addVBox());
        myRoot.getChildren().add(borderPane);
    }

    private VBox addVBox(){
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE, VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE));
        vbox.setSpacing(VBOX_BUFFER_BUTTON);
        vbox.setStyle("-fx-background-color: #40E0D0");
        return vbox;
    }

    private Button resetButton(){
        Button resetButton = new Button();
        return resetButton;
    }
}
