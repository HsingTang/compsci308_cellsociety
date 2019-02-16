package CellSociety;

import java.util.ArrayList;

/**
 * @author Carrie Hunner
 * This is a cell concrete implementation that uses the rules of the Game of Life simulation.
 */
public class CellGameOfLife extends Cell {

    private static final String DEAD = "Dead";
    private static final String ALIVE = "Alive";
    private static final int THRESHOLD_ALIVE = 3;
    private static final int THRESHOLD_DEAD = 2;
    private int numNeighborsAlive;

    /**
     * @param row int index of the row of the cell in a grid of cells that will be passed through
     *            when setting neighbors
     * @param col int index of the column of the cell in a grid of cells that will be passed through when]
     *            setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters empty ArrayList that is not needed for this concrete implementation
     */
    CellGameOfLife(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);
    }

    //adds all possible states to instance variable
    @Override
    protected void initializeStatesList() {
        myStates.add(DEAD);
        myStates.add(ALIVE);
    }


    /**
     * Calls the cell to look at its neighbors' states and determine its next state according to that and
     * its rules.
     */
    @Override
    public void findNextState() {
        findNumNeighborsAlive();
            switch(myCurrentState){
                case DEAD:
                    if(numNeighborsAlive == THRESHOLD_ALIVE){
                        myNextState = ALIVE;
                    }
                case ALIVE:
                    if(numNeighborsAlive < THRESHOLD_DEAD){
                        myNextState = DEAD;
                    }
                    else if(numNeighborsAlive <= THRESHOLD_ALIVE){
                        myNextState = ALIVE;
                    }
                    else if(numNeighborsAlive > THRESHOLD_ALIVE){
                        myNextState = DEAD;
                    }
            }
    }


    private void handleDead() {
        if(numNeighborsAlive == THRESHOLD_ALIVE){
            myNextState = ALIVE;
        }
        else{
            myNextState = DEAD;
        }
    }

    private void handleAlive() {
        if(numNeighborsAlive < THRESHOLD_DEAD){
            myNextState = DEAD;
        }
        else if(numNeighborsAlive == THRESHOLD_ALIVE){
            myNextState = ALIVE;
        }
        else if(numNeighborsAlive > THRESHOLD_ALIVE){
            myNextState = DEAD;
        }
    }

    //determines how many neighbors are alive
    private void findNumNeighborsAlive(){
        numNeighborsAlive = 0;
        for(Cell c : myNeighbors){
            switch(c.getState()){
                case ALIVE:
                    numNeighborsAlive++;
            }
        }
    }
}
