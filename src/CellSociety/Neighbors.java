package CellSociety;

import java.util.ArrayList;
import java.util.HashMap;

abstract public class Neighbors {
    protected int myRow;
    protected int myCol;
    protected ArrayList<ArrayList<Cell>> myGrid;

    protected ArrayList<Integer> myNeighborIndexes;
    protected String myEdgeType;

    protected ArrayList<Cell> myNeighbors;
    protected HashMap<Integer, ArrayList<Integer>> myIndexMap;

    private final String FINITE = "Finite";
    private final String TOROIDAL = "Toroidal";


    Neighbors(int row, int col, ArrayList<ArrayList<Cell>> grid){
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

    private boolean isValidEdgeType(String edgeType){
        switch (edgeType){
            case FINITE:
                return true;
            case TOROIDAL:
                return true;
        }
        return false;
    }
}
