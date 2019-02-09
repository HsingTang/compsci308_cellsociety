package CellSociety;

import java.util.ArrayList;

public class NeighborsSquare extends Neighbors {


    NeighborsSquare(int row, int col, Cell[][] grid) {
        super(row, col, grid);
    }

    @Override
    protected void setIndexMap() {
        int[] dRow = new int[] {-1, 0, 1};
        int[] dCol = new int[] {-1, 0, 1};

        Integer key = -1;
        //System.out.println("Row: " + myRow + " Col: " + myCol);
        for(int k = 0; k < dRow.length; k++){
            for(int i = 0; i < dCol.length; i++){
                key++;
                int tempRow = dRow[k] + myRow;
                int tempCol = dCol[i] + myCol;

                //ensures not to add self
                if(tempRow == myRow && tempCol == myCol){
                    continue;
                }
                if(inBounds(tempRow, tempCol)){
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(tempRow);
                    temp.add(tempCol);

                    myIndexMap.put(key, temp);
                    //System.out.println("My Neighbor Row: " + tempRow + " Col: " + tempCol);
                }
                else{
                    if(myEdgeType.equals(TOROIDAL)){
                        myIndexMap.put(key, findToroidalCoords(tempRow, tempCol));
                    }
                }

            }

        }
    }

    private ArrayList<Integer> findToroidalCoords(int row, int col){
        int wrappedRow;
        int wrappedCol;

        if(row >= myGrid.length){
            wrappedRow = row - myGrid.length;
        }

        else if(row < 0){
            wrappedRow = myGrid.length + row;
        }
        else{
            throw new IndexOutOfBoundsException("Row was within bounds of the grid");
        }

        if(col >= myGrid[0].length){
            wrappedCol = col - myGrid[0].length;
        }
        else if(col < 0){
            wrappedCol = myGrid[0].length + col;
        }
        else{
            throw new IndexOutOfBoundsException("Col was within bounds of grid");
        }

        ArrayList<Integer> coords = new ArrayList<>();
        coords.add(wrappedRow);
        coords.add(wrappedCol);
        return coords;
    }

    @Override
    protected void setNeighbors() {
        for(Integer index : myNeighborIndexes){
            ArrayList<Integer> coords = myIndexMap.get(index);
            int row = coords.get(0);
            int col = coords.get(1);

            myNeighbors.add(myGrid[row][col]);
        }
    }
}
