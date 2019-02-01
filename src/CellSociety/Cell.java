package CellSociety;
//look into enum

import java.util.ArrayList;
import java.util.List;


abstract class Cell {
    private String myCurrentState;
    private String myNextState;
    private int myCol;
    private int myRow;
    private List<Double> myParams;
    private List<Cell> myNeighbors;
    private Cell[][] myGrid;

    Cell(int row, int col, String initialState, ArrayList<Double> parameters){
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
     * This method finds and sets the Cell's neighbors based on the 2D Cell array passed as an argument.
     * @param Cell 2D int array of Cell objects
     */
    //Note: it will be set in each implementation so the concrete classes can choose if they want to call
    //a method for 4 or 8 neighbors, or they can call another method entirely.
    abstract public void findNeighbors(Cell[][] Cell);

    //generates and sets 4 neighbors
    //can be called by concrete class implementations
    private void generateFourNeighbors(){
        int[] dRow = new int[] {0, 0, 1, -1};
        int[] dCol = new int[] {1, -1, 0, 0};

        for(int k = 0; k < dRow.length; k++){
            int tempRow = dRow[k] + myRow;
            int tempCol = dCol[k] + myCol;

            if(inBounds(tempRow, tempCol)){
                myNeighbors.add(myGrid[tempRow][tempCol]);
            }
        }
    }

    //generates and sets 8 neighbors
    //can be called by concrete class implementations
    private void generateEightNeighbors(){
        int[] dRow = new int[] {-1, 0, 1};
        int[] dCol = new int[] {-1, 0, 1};

        for(int k = 0; k < dRow.length; k++){
            for(int i = 0; i < dCol.length; i++){
                int tempRow = dRow[k] + myRow;
                int tempCol = dCol[i] + myCol;

                //ensures not to add self
                if(tempRow == 0 && tempCol == 0){
                    continue;
                }
                else{
                    if(inBounds(tempRow, tempCol)){
                        myNeighbors.add(myGrid[tempRow][tempCol]);
                    }
                }
            }
        }
    }

    //checks if indices are within the grid
    private boolean inBounds(int row, int col){
        if(row < 0 || row > myGrid.length){
            return false;
        }
        else if(col < 0 || col > myGrid[0].length){
            return false;
        }
        return true;
    }
}
