package CellSociety;

import java.util.ArrayList;
import java.util.Random;

public class CellFire extends Cell {

    private final String BURNING = "Burning";
    private final String TREE = "Tree";
    private final String EMPTY = "Empty";

    private Random myRand;

    private double myProbBurning;

    CellFire(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);
        myProbBurning = parameters.get(0);
        myRand = new Random();
    }

    @Override
    protected void initializeStatesList() {
        myStates.add(BURNING);
        myStates.add(TREE);
        myStates.add(EMPTY);
    }

    /**
     * Calls the cell to look at its neighbors' states and determine its next state according to that and
     * its rules for burning.
     */
    @Override
    public void findNextState() {
        switch(myCurrentState){
            case BURNING:
                myNextState = EMPTY;
                break;
            case EMPTY:
                myNextState = EMPTY;
                break;
            case TREE:
                checkNeighborsAndBurning();
        }
    }

    //handles burning neighbors and setting next state of tree
    private void checkNeighborsAndBurning() {
        for(Cell c : myNeighbors){
            if(c.getState().equals(BURNING) && myRand.nextFloat() < myProbBurning){
                myNextState = BURNING;
                break;
            }
            myNextState = TREE;
        }
    }

    /**
     * Finds and sets neighbors.
     * Assumes the cell can have up to four neighbors.
     * @param cell 2D int array of Cell objects
     */
    @Override
    public void findNeighbors(Cell[][] cell) {
        myGrid = cell;
        generateFourNeighbors();
    }
}
