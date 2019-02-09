package CellSociety.Neighbors;

import CellSociety.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

abstract public class Neighbors {
    protected int myRow;
    protected int myCol;
    protected Cell[][] myGrid;

    protected List<Integer> myNeighborIndexes;
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
    public void initializeEdgeAndIndexes(String edgeType, List<Integer> neighborIndexes){
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

    protected void handleEdgesAndAddCoords(Integer key, int tempRow, int tempCol) {
        if (inBounds(tempRow, tempCol)) {
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(tempRow);
            temp.add(tempCol);

            myIndexMap.put(key, temp);
            //System.out.println("My Neighbor Row: " + tempRow + " Col: " + tempCol);
        } else {
            if (myEdgeType.equals(TOROIDAL)) {
                myIndexMap.put(key, findToroidalCoords(tempRow, tempCol));
            }
        }
    }

    private ArrayList<Integer> findToroidalCoords(int row, int col) {
        int wrappedRow;
        int wrappedCol;

        if (row >= myGrid.length) {
            wrappedRow = row - myGrid.length;
        } else if (row < 0) {
            wrappedRow = myGrid.length + row;
        } else {
            throw new IndexOutOfBoundsException("Row was within bounds of the grid");
        }

        if (col >= myGrid[0].length) {
            wrappedCol = col - myGrid[0].length;
        } else if (col < 0) {
            wrappedCol = myGrid[0].length + col;
        } else {
            throw new IndexOutOfBoundsException("Col was within bounds of grid");
        }

        ArrayList<Integer> coords = new ArrayList<>();
        coords.add(wrappedRow);
        coords.add(wrappedCol);
        return coords;
    }

    abstract protected void setIndexMap();

    private void setDesiredNeighbors(){
        for (Integer index : myNeighborIndexes) {
            if (myIndexMap.containsKey(index)) {
                ArrayList<Integer> coords = myIndexMap.get(index);
                int row = coords.get(0);
                int col = coords.get(1);

                myNeighbors.add(myGrid[row][col]);
            }
        }
    };


}
