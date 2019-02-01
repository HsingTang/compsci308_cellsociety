package CellSociety;
//look into enum

import java.util.ArrayList;
import java.util.List;


abstract class Cell {
    private String myCurrentState;
    private String myNextState;
    private int myCol;
    private int myRow;
    private List<Integer> myParams;

    Cell(int row, int col, String initialState, ArrayList<Integer> parameters){
        myRow = row;
        myCol = col;
        myCurrentState = initialState;
        myParams = parameters;
    }

    /**
     * Calls the cell to use its neighbors and its rules to determine what its next state should be
     */
    abstract public void findNextState();


    /**
     * @return String corresponding to the cell's current state
     */
    public String getState(){
     return myCurrentState;
    }

    /**
     * Updates the cell's current state to its next state
     */
    public void updateState(){
        myCurrentState = myNextState;
    }


    /**
     * it will take in the 2D array and then locate and set its neighbors
     * @param Cell 2D int array of Cell objects
     */
    abstract public void findNeighbors(int[][] Cell);
}
