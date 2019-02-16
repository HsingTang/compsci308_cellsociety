package CellSociety.Neighbors;

import CellSociety.Cell;

import java.util.ArrayList;

/**
 * @author Carrie Hunner
 * This class handles finding the neighbors for a square cell.
 *
 * After the Neighbors class is instantiated, the initializeAndSetEdgeType method
 * needs to be called.
 */
public class NeighborsSquare extends Neighbors {


    /**
     * Sets all of the instance variables for the Neighbor class.
     * @param row   int of the row index of the current cell
     * @param col   int of the cell index of the current cell
     * @param grid  2D cell array grid of all the cells
     */
    public NeighborsSquare(int row, int col, Cell[][] grid) {
        super(row, col, grid);
    }

//finds all possible neighbors and creates a map of them with their indexes being the key and
    //their row and columns coordinates being in an arraylist in the value
    @Override
    protected void setIndexMap() {
        int[] dRow = new int[]{-1, 0, 1};
        int[] dCol = new int[]{-1, 0, 1};
        ////System.out.println("Setting Index map for Row: " + myRow + " Col: " + myCol);
        Integer key = -1;
        ////System.out.println("Row: " + myRow + " Col: " + myCol);
        for (int k = 0; k < dRow.length; k++) {
            for (int i = 0; i < dCol.length; i++) {

                key++;
                int tempRow = dRow[k] + myRow;
                int tempCol = dCol[i] + myCol;



                //ensures not to add self
                if (tempRow == myRow && tempCol == myCol) {
                    ////System.out.println("\t Was self and allegedly continues right after this");
                    ////System.out.println("\t Found Self and ignored (" + tempRow + ", " + tempCol + ")");
                    key--;  //key shouldn't increment for self
                    continue;
                }
                ////System.out.println("\t attempting handle edges");
                handleEdgesAndAddCoords(key, tempRow, tempCol);
                ////System.out.println("\t succeeded in handling edges");

            }

        }
    }




}
