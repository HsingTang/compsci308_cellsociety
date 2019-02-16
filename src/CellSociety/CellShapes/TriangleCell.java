package CellSociety.CellShapes;

import CellSociety.UI;

public class TriangleCell extends CellShape{
    private final static int NUM_COORDINATES = 6;
    private final static double[] STARTING_COORDINATES = new double[NUM_COORDINATES];

    public TriangleCell (UI ui){
        super(STARTING_COORDINATES, ui);
        setStartingCoordinates();
    }

    private void setStartingCoordinates(){

    }

}
