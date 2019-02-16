package CellSociety.CellShapes;
import CellSociety.UI;

public class SquareCell extends CellShape {
    private final static int NUM_COORDINATES = 8;
    private final static double[] STARTING_COORDINATES = new double[NUM_COORDINATES];

    public SquareCell(UI ui){
        super(STARTING_COORDINATES, ui);
        setStartingCoordinates();

    }

    private void setStartingCoordinates(){
        STARTING_COORDINATES[0] = 0;
        STARTING_COORDINATES[1] = 0;
        STARTING_COORDINATES[2] = 0;
        STARTING_COORDINATES[3] = super.CELL_HEIGHT;
        STARTING_COORDINATES[4] = super.CELL_WIDTH;
        STARTING_COORDINATES[5] = super.CELL_HEIGHT;
        STARTING_COORDINATES[6] = super.CELL_WIDTH;
        STARTING_COORDINATES[7] = 0;

    }
}
