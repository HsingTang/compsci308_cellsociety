package CellSociety.CellShapes;

import CellSociety.UI;
import javafx.scene.shape.Polygon;

public class SquareCell extends CellShape {
    private final static int SQUARE_NUM_COORDINATES = 8;
    private final double[] STARTING_COORDINATES = new double[]{
            0, 0,
            0, CELL_HEIGHT,
            CELL_WIDTH, CELL_HEIGHT,
            CELL_WIDTH, 0
    };

    private double[] myCoordinates;
    public SquareCell(UI ui, int row, int col){
        super(ui, row, col);
        myCoordinates = new double[SQUARE_NUM_COORDINATES];
        assignCoordinates(myCoordinates, SQUARE_NUM_COORDINATES);
        myShape = new Polygon(myCoordinates);
    }

    @Override
    protected double calcXCoordinate(int i){
        double x = STARTING_COORDINATES[i] + COL * CELL_WIDTH;
        return x;
    }

    @Override
    protected double calcYCoordinate(int i){
        double y = STARTING_COORDINATES[i] + ROW * CELL_HEIGHT;
        return y;
    }

}
