package CellSociety.Neighbors;

import CellSociety.Cell;

import java.util.ArrayList;
import java.util.HashMap;

abstract public class Neighbors {
    protected int myRow;
    protected int myCol;
    protected Cell[][] myGrid;

    protected ArrayList<Integer> myNeighborIndexes;
    protected String myEdgeType;

    protected ArrayList<Cell> myNeighbors;
    protected HashMap<Integer, ArrayList<Integer>> myIndexMap;

    protected final String FINITE = "Finite";
    protected final String TOROIDAL = "Toroidal";


    Neighbors(int row, int col, Cell[][] grid){
        myRow = row;
        myCol = col;
        myGrid = grid;
    }

    /**
     * Checks if edge type is valid and then sets edge type and neighbor indexes.
     * @param edgeType String indicating the edge type (finite, toroidal)
     * @param neighborIndexes ArrayList of Indexes corresponding to desired neighbors
     */
    public void initializeEdgeAndIndexes(String edgeType, ArrayList<Integer> neighborIndexes){
        if(isValidEdgeType(edgeType)){
            myEdgeType = edgeType;
        }
        else{
            throw new IllegalArgumentException("Not a valid edgetype");
        }

        myNeighborIndexes = neighborIndexes;
    }

    public ArrayList<Cell> getNeighborsList(){
        setIndexMap();
        setDesiredNeighbors();

        return myNeighbors;
    }

    private boolean isValidEdgeType(String edgeType){
        switch (edgeType){
            case FINITE:
                return true;
            case TOROIDAL:
                return true;
        }
        return false;
    }

    //checks if indices are within the grid
    protected boolean inBounds(int row, int col){
        if(row < 0 || row >= myGrid.length){
            return false;
        }
        else if(col < 0 || col >= myGrid[0].length){
            return false;
        }
        return true;
    }

    abstract protected void setIndexMap();

    abstract protected void setDesiredNeighbors();


}
