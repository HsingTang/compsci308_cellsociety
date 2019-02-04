package CellSociety;

import java.util.ArrayList;

public class CellSegregation extends Cell {
    private final String GROUP1 = "Group1";
    private final String GROUP2 = "Group2";
    private final String EMPTY = "Empty";

    private double myThreshold;
    private double mySatisfaction;


    CellSegregation(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);

        myThreshold = parameters.get(0);
    }


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
        if(mySatisfaction > myThreshold){
            myNextState = myCurrentState;
            //System.out.println("Stayed Put" + myNextState);
        }
        else{
            findAndSetNewLocation();
            myNextState = EMPTY;
        }
    }

    private void findAndSetNewLocation() {
        int tempRow;
        int tempCol = myCol;

        //dealing with getting to the end of the current row
        for(tempRow = myRow; tempRow < myGrid.length; tempRow++){
            if(foundAndSetNextLoc(tempRow, tempCol)){
                return;
            }
        }

        //dealing with getting to end of grid
        for(tempCol += 1; tempCol < myGrid[0].length; tempCol++){
            for(tempRow = 0; tempRow < myGrid.length; tempRow++){
                if(foundAndSetNextLoc(tempRow, tempCol)){
                    return;
                }
            }
        }

        //going to end of grid
        for(tempCol = 0; tempCol < myCol; tempCol++){
            for(tempRow = 0; tempRow < myRow; tempRow++){
                if(foundAndSetNextLoc(tempRow, tempCol)){
                    return;
                }
            }
        }

        myNextState = myCurrentState;
    }

    private boolean isEmpty(Cell c){
        if(c.getState().equals(EMPTY)){
            //makes sure it's not already claimed
            if(c.getNextState().equals("") || c.getNextState().equals(EMPTY)){
                return true;
            }
        }
        return false;
    }

    private boolean foundAndSetNextLoc(int row, int col){
        Cell temp = myGrid[row][col];
        if(isEmpty(temp)){
            temp.setNextState(myCurrentState);
            return true;
        }
        return false;
    }

    private void calcSatisfaction() {
        double numPop1 = 0;
        double numPop2 = 0;
        for(Cell c : myNeighbors){
            switch(c.getState()){
                case GROUP1:
                    numPop1 += 1.0;
                case GROUP2:
                    numPop2 += 1.0;
            }
        }
        double tot = numPop1 + numPop2;
        switch(myCurrentState){
            case GROUP1:
                mySatisfaction = numPop1/tot;
            case GROUP2:
                mySatisfaction = numPop2/tot;
        }
    }

    /**
     * Finds and sets up to eight neighbors.
     * @param cell 2D int array of Cell objects
     */
    @Override
    public void findNeighbors(Cell[][] cell) {
        myGrid = cell;
        generateEightNeighbors();
    }
}
