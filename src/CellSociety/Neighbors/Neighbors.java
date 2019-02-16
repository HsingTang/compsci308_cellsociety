package CellSociety.Neighbors;

import CellSociety.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Carrie Hunner
 * This class is a superclass that is designed to have concrete implementations
 * such that it can find all possible neighbors for a shape, take in a list of
 * desired neighbor indexes, and return a list of the indexes of those neighbors
 * when called.
 *
 * After the Neighbors class is instantiated, the initializeAndSetEdgeType method
 * needs to be called.
 */
abstract public class Neighbors {
    protected int myRow;
    protected int myCol;
    protected Cell[][] myGrid;

    protected List<Integer> myNeighborIndexes;
    protected String myEdgeType;

    protected List<Cell> myNeighbors;
    protected HashMap<Integer, ArrayList<Integer>> myIndexMap;

    protected static final String FINITE = "Finite";
    protected static final String TOROIDAL = "Toroidal";


    /**
     * Sets all of the instance variables for the Neighbor class.
     * @param row   int of the row index of the current cell
     * @param col   int of the cell index of the current cell
     * @param grid  2D cell array grid of all the cells
     */
    Neighbors(int row, int col, Cell[][] grid){
        myRow = row;
        myCol = col;
        myGrid = grid;

        myNeighborIndexes = new ArrayList<>();
        myNeighbors = new ArrayList<>();
        myIndexMap = new HashMap<>();
    }

    /**
     * Checks if edge type is valid and then sets edge type and neighbor indexes.
     * @param edgeType String indicating the edge type (finite, toroidal)
     * @param neighborIndexes ArrayList of Indexes corresponding to desired neighbors
     */
    public void initializeEdgeAndIndexes(String edgeType, List<Integer> neighborIndexes){
        if(isValidEdgeType(edgeType)){
            myEdgeType = edgeType;
        }
        else{
            throw new IllegalArgumentException("Not a valid edgetype");
        }
        myNeighborIndexes = neighborIndexes;
    }

    /**
     * Find the neighbors of the current cell and returns them in a list.
     * @return List of Cells that are the current cell's desired neighbors.
     */
    public List<Cell> getNeighborsList(){
        setIndexMap();
        setDesiredNeighbors();
        return myNeighbors;
    }

    //checks if the edge type is valid
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

    //deals with edges, finite and toroidal
    protected void handleEdgesAndAddCoords(Integer key, int tempRow, int tempCol) {
        if (inBounds(tempRow, tempCol)) {
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(tempRow);
            temp.add(tempCol);
            myIndexMap.put(key, temp);
        } else {
            if (myEdgeType.equals(TOROIDAL)) {
                myIndexMap.put(key, findToroidalCoords(tempRow, tempCol));
            }
        }
    }

    //finds the coordinates for the edge neighbors with toroidal rules
    private ArrayList<Integer> findToroidalCoords(int row, int col) {
        int wrappedRow;
        int wrappedCol;
        wrappedRow = getWrappedIndex(row, myGrid.length);
        wrappedCol = getWrappedIndex(col, myGrid[0].length);
        ArrayList<Integer> coords = new ArrayList<>();
        coords.add(wrappedRow);
        coords.add(wrappedCol);
        return coords;
    }

    //return a wrapped index for toroidal edges
    private int getWrappedIndex(int index, int maxSize) {
        int wrappedIndex;
        if (index >= maxSize) {
            wrappedIndex = index - maxSize;
        } else if (index < 0) {
            wrappedIndex = maxSize + index;
        } else {
            wrappedIndex = index;
        }
        return wrappedIndex;
    }

    //sets up the map of all possible neighbors for the shape type
    abstract protected void setIndexMap();

    //finds the desired neighbors in the index map and adds them to a list
    private void setDesiredNeighbors(){
        for (Integer index : myNeighborIndexes) {
            if (myIndexMap.containsKey(index)) {
                ArrayList<Integer> coords = myIndexMap.get(index);
                int row = coords.get(0);
                int col = coords.get(1);
                myNeighbors.add(myGrid[row][col]);
            }
        }
    }
}
