package CellSociety;
//look into enum

import CellSociety.Neighbors.NeighborsSquare;

import java.util.ArrayList;
import java.util.List;


public abstract class Cell {
    protected String myCurrentState;
    protected String myNextState;
    protected int myCol;
    protected int myRow;
    protected List<Double> myParams;
    protected List<Cell> myNeighbors;
    protected Cell[][] myGrid;
    protected List<String> myStates;

    private final String SQUARE = "Square";
    private final String TRIANGLE = "Triangle";

    /**
     * @author Carrie Hunner (clh87)
     *
     * @param row int index of the row of the cell in a grid of cells that will be passed through
     *            when setting neighbors
     * @param col int index of the column of the cell in a grid of cells that will be passed through when]
     *            setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters ArrayList of doubles containing any extra parameters needed e.g. probability
     *                   of catching fire or the health of a shark
     */
    Cell(int row, int col, String initialState, ArrayList<Double> parameters){
        myRow = row;
        myCol = col;
        myCurrentState = initialState;
        myNeighbors = new ArrayList<>();
        myParams = parameters;
        myNextState = "";

        myStates = new ArrayList<>();
        initializeStatesList();
    }

    //each implementation has to create a List of possible states
    abstract protected void initializeStatesList();

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
        myNextState = "";       //can be used in concrete classes to check if a nextState has been set
    }


    /**
     * Used to set the neighbors of the Cell.
     * @param cell ArrayList of ArrayLists of Cells, makes up the grid of cells
     * @param neighborIndexes ArrayList of integers corresponding with neighbor indexes
     */
    //Note: it will be set in each implementation so the concrete classes can choose if they want to call
    //a method for 4 or 8 neighbors, or they can call another method entirely.
    public void findNeighbors(Cell[][] cell, String shapeType, String edgeType, ArrayList<Integer> neighborIndexes){
        myGrid = cell;
        switch(shapeType){
            case SQUARE:
                NeighborsSquare neighbors = new NeighborsSquare(myRow, myCol, myGrid);
                neighbors.initializeEdgeAndIndexes(edgeType, neighborIndexes);
                myNeighbors =  neighbors.getNeighborsList();
            case TRIANGLE:
        }
        throw new IllegalArgumentException("Unknown Shape Type");

    }


    /**
     * Used in WATOR and Segregation to determine if a cell has been claimed for the
     * following step.
     * @return String of the cell's next state.
     */
    public String getNextState(){
        return myNextState;
    }

    /**
     * Used in WATOR and Segregation to handle the movement of people/animals.
     * @param state String of the desired next state;
     */
    public void setNextState(String state){
        myNextState = state;
    }


}
