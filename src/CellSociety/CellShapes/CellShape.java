package CellSociety.CellShapes;

import CellSociety.UI;
import javafx.scene.shape.Polygon;

abstract public class CellShape {
    protected final int CELL_WIDTH;
    protected final int CELL_HEIGHT;
    protected final int ROW;
    protected final int COL;
    protected Polygon myShape;

    public CellShape (UI myUI, int row, int col){
        CELL_WIDTH = myUI.GRID_HEIGHT/myUI.GRID_ROW_NUM;
        CELL_HEIGHT = myUI.GRID_WIDTH/myUI.GRID_COL_NUM;
        ROW = row;
        COL = col;
    }

    protected void assignCoordinates(double[] coordinates, int numCoordinates) {
        for (int i = 0; i < numCoordinates; i++) {
            if (i % 2 == 0){ //assign x coordinate
                coordinates[i] = calcXCoordinate(i);
            }
            else {
                coordinates[i] = calcYCoordinate(i);
            }
        }
    }

    abstract protected double calcXCoordinate(int i);

    abstract protected double calcYCoordinate(int i);

    public Polygon getMyShape(){
        return myShape;
    }
}
