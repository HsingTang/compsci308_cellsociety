package CellSociety;

import java.util.ArrayList;

public class CellPercolation extends Cell {
    private final String OPEN  = "Open";
    private final String BLOCKED = "Blocked";
    private final String PERCOLATED = "Percolated";



    CellPercolation(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);
    }

    @Override
    protected void initializeStatesList() {
        myStates.add(OPEN);
        myStates.add(BLOCKED);
        myStates.add(PERCOLATED);
    }

    /**
     * Finds and sets the next state of the cell by checking neighbors
     * and following the rules set.
     */
    @Override
    public void findNextState() {
        if(myCurrentState.equals(OPEN)) {
            checkNeighborsAndPercolate();
        }
        else{
            myNextState = myCurrentState;
        }

    }

    //tests neigh
    private void checkNeighborsAndPercolate() {
        for (Cell c : myNeighbors) {
            if (c.getState().equals(PERCOLATED)) {
                myNextState = PERCOLATED;
                break;
            }
            else{
                myNextState = OPEN;
            }
        }
    }

    /**
     * Locates and sets the neighbors using the cell array argument.
     * For Precolation, each cell has eight neighbors.
     * @param cell 2D int array of Cell objects
     */
    @Override
    public void findNeighbors(Cell[][] cell) {
        myGrid = cell;
        generateEightNeighbors();
    }
}
