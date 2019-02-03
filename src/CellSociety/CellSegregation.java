package CellSociety;

import java.util.ArrayList;

public class CellSegregation extends Cell {
    private final String POPULATION1 = "Population1";
    private final String POPULATION2 = "Population2";
    private final String EMPTY = "Empty";

    private double myThreshold;
    private double mySatisfaction;


    CellSegregation(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);

        myThreshold = parameters.get(0);
    }


    @Override
    protected void initializeStatesList() {
        myStates.add(POPULATION1);
        myStates.add(POPULATION2);
        myStates.add(EMPTY);
    }

    /**
     * Finds and sets the next state of the cell by checking neighbors
     * and following the rules set.
     */
    @Override
    public void findNextState() {
        if(myCurrentState.equals(EMPTY) && myNextState.equals("")){
            myNextState = EMPTY;
        }
        calcSatisfaction();

        if(mySatisfaction > myThreshold){
            myNextState = myCurrentState;
        }
        else{
            findAndSetNewLocation();
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
        float numPop1 = 0;
        float numPop2 = 0;
        for(Cell c : myNeighbors){
            switch(c.getState()){
                case POPULATION1:
                    numPop1++;
                case POPULATION2:
                    numPop2++;
            }
        }
        switch(myCurrentState){
            case POPULATION1:
                mySatisfaction = numPop1/numPop2;
            case POPULATION2:
                mySatisfaction = numPop2/numPop1;
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
