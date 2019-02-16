package CellSociety;

import java.util.ArrayList;

/**
 * @author Carrie Hunner
 * This is a cell concrete implementation that uses the rules of the Segregation simulation.
 */
public class CellSegregation extends Cell {
    private static final String GROUP1 = "Group1";
    private static final String GROUP2 = "Group2";
    private static final String EMPTY = "Empty";
    private static final int THRESHOLD_INDEX = 0;

    private double myThreshold;
    private double mySatisfaction;


    /**
     * @param row int index of the row of the cell in a grid of cells that will be passed through
     *            when setting neighbors
     * @param col int index of the column of the cell in a grid of cells that will be passed through when]
     *            setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters ArrayList of doubles with the threshold frequency as a decimal value
     */
    CellSegregation(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);
        setParams();
    }

    /**
     * Sets the parameters of the simulation.
     */
    @Override
    protected void setParams(){
        myThreshold = myParams.get(THRESHOLD_INDEX);
    }


    //adds all possible states to a list
    @Override
    protected void initializeStatesList() {
        myStates.add(GROUP1);
        myStates.add(GROUP2);
        myStates.add(EMPTY);
    }

    /**
     * Finds and sets the next state of the cell by checking neighbors
     * and following the rules set.
     */
    @Override
    public void findNextState() {
        setParams();
        //if it's empty and hasn't had anything overwrite it yet
        if(myCurrentState.equals(EMPTY) && myNextState.equals("")){
            myNextState = EMPTY;
            return;
        }
        //if it's nextstate has already been set externally by another cell
        else if(!myNextState.equals("")){
            return;
        }
        calcSatisfaction();
        if(mySatisfaction >= myThreshold){
            myNextState = myCurrentState;
            return;
        }
        else{
            findAndSetNewLocation();
        }
    }

    //indexes through the grid to find the next empty and unclaimed location
    //starts at current location, and goes to the bottom of the grid from left to right,
    //top to bottom. Then it starts at the top of the grid and scrolls until the initial lccation is found.
    private void findAndSetNewLocation() {
        int tempRow = myRow;

        //dealing with getting to the end of the current row
        if(scrollThroughToBounds(myRow, myRow+1, myGrid[0].length)) return;
        //scrolls to end of grid
        if (scrollThroughToBounds(tempRow, myGrid.length, myGrid[0].length)) return;
        //scrolls from top to bottom
        if(scrollThroughToBounds(0, myRow, myCol)) return;
        myNextState = myCurrentState;
    }

    //scroll through grid from input row and col 0
    //up until row and column bounds
    //checking if a new location can be set
    private boolean scrollThroughToBounds(int row, int rowBound, int colBound) {
        int tempCol;
        for(row += 1; row < rowBound; row++){
            for(tempCol = 0; tempCol < colBound; tempCol++){
                if(foundAndSetNextLoc(row, tempCol)){
                    return true;
                }
            }
        }
        return false;
    }

    //checks if the cell is currently empty and it not yet claimed for the following cycle
    private boolean isEmpty(Cell c){
        if(c.getState().equals(EMPTY) && notClaimed(c)){
            return true;
        }
        return false;
    }

    private boolean notClaimed(Cell c) {
        return (c.getNextState().equals("") || c.getNextState().equals(EMPTY));
    }

    //checks if it the cell is available and if it is, sets that as its next location
    private boolean foundAndSetNextLoc(int row, int col){
        Cell temp = myGrid[row][col];
        if(isEmpty(temp)){
            temp.setNextState(myCurrentState);
            myNextState = EMPTY;
            return true;
        }
        return false;
    }

    //calculates the satisfaction of the cell
    private void calcSatisfaction() {
        double numPop1 = 0;
        double numPop2 = 0;
        for(Cell c : myNeighbors){
            switch(c.getState()){
                case GROUP1:
                    numPop1 += 1.0;
                    break;
                case GROUP2:
                    numPop2 += 1.0;
                    break;
            }
        }
        double tot = numPop1 + numPop2;
        switch(myCurrentState){
            case GROUP1:
                mySatisfaction = numPop1/tot;
                break;
            case GROUP2:
                mySatisfaction = numPop2/tot;
                break;
        }
        if(!(mySatisfaction >= 0)){
            mySatisfaction = 0.0;
        }
    }
}
