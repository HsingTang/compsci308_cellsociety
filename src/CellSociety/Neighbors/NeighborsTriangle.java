package CellSociety.Neighbors;

import CellSociety.Cell;

/**
 * @author Carrie Hunner
 * This class handles finding the neighbors for a triangular cell.
 *
 * After the Neighbors class is instantiated, the initializeAndSetEdgeType method
 * needs to be called.
 */
public class NeighborsTriangle extends Neighbors {
    private boolean isPointingUp;
    //downward oriented
    private static final int[] downTop= {-2, -1, 0, 1, 2};
    private static final int[] downCenter = {-2, -1, 1, 2};
    private static final int[] downBottom = {-1, 0, 1};
    //upward oriented
    private static final int[] upBottom = {2, 1, 0, -1, -2};
    private static final int[] upCenter = {2, 1, -1, -2};
    private static final int[] upTop =  {1, 0, -1};


    /**
     * Sets all of the instance variables for the Neighbor class.
     * @param row   int of the row index of the current cell
     * @param col   int of the cell index of the current cell
     * @param grid  2D cell array grid of all the cells
     */
    public NeighborsTriangle(int row, int col, Cell[][] grid) {
        super(row, col, grid);
    }

    //finds all possible neighbors and creates a map of them with their indexes being the key and
    //their row and columns coordinates being in an arraylist in the value
    @Override
    protected void setIndexMap() {
        setOrientation();
        int index = -1;
        if(isPointingUp){
            index = addNeighbors(index, upBottom, myRow + 1);
            index = addNeighbors(index, upCenter, myRow);
            addNeighbors(index, upTop, myRow - 1);
        }
        else{
            index = addNeighbors(index, downTop, myRow - 1);
            index = addNeighbors(index, downCenter, myRow);
            addNeighbors(index, downBottom, myRow + 1);
        }
    }

    //cycles through a row and adds the neighbors to the hashmap of neighbors
    private int addNeighbors(int index, int[] dCol, int row){
        for(int k = 0; k < dCol.length; k++){
            int tempCol = myCol + dCol[k];
            index++;
            handleEdgesAndAddCoords(index, row, tempCol);
        }
        return index;
    }

    //used to determine the orientation of the triangle, as that affects the location of the neighbors
    private void setOrientation(){
        int sum = myRow + myCol;
        if(sum == 0 || (sum %2 == 0)){
            isPointingUp = true;
        }
        else{
            isPointingUp = false;
        }
    }
}
