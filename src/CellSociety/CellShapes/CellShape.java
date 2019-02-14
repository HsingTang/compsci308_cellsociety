package CellSociety.CellShapes;

import CellSociety.UI;
import javafx.scene.shape.Polygon;

abstract public class CellShape extends Polygon {
    protected UI myUI;
    protected int CELL_WIDTH;
    protected int CELL_HEIGHT;

    public CellShape (double[] coordinates, UI ui){
        super(coordinates);
        myUI = ui;
        setCellDimensions();
    }

    protected void setCellDimensions(){
        CELL_WIDTH = myUI.GRID_HEIGHT/myUI.GRID_ROW_NUM;
        CELL_HEIGHT = myUI.GRID_WIDTH/myUI.GRID_COL_NUM;
    }
}
