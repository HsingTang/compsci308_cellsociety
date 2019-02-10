package CellSociety;

import java.util.ArrayList;
import java.util.Random;

public class CellRPS extends Cell {
    private final String WHITE = "White";
    private final String RED = "Red";
    private final String GREEN = "Green";
    private final String BLUE = "Blue";

    private Random myRand;


    /**
     * @param row          int index of the row of the cell in a grid of cells that will be passed through
     *                     when setting neighbors
     * @param col          int index of the column of the cell in a grid of cells that will be passed through when]
     *                     setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters   ArrayList of doubles containing any extra parameters needed e.g. probability
     * @author Carrie Hunner (clh87)
     */
    CellRPS(int row, int col, String initialState, ArrayList<Double> parameters) {
        super(row, col, initialState, parameters);
        myRand = new Random();
    }

    @Override
    protected void initializeStatesList() {
        myStates.add(WHITE);
        myStates.add(RED);
        myStates.add(GREEN);
        myStates.add(BLUE);
    }

    @Override
    public void findNextState() {
        int neighborIndex = myRand.nextInt(myNeighbors.size());

        



    }
}
