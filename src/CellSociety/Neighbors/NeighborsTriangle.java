package CellSociety.Neighbors;

import CellSociety.Cell;

public class NeighborsTriangle extends Neighbors {
    private boolean isPointingUp;
    private final int[] downTop= {-2, -1, 0, 1, 2};
    private final int[] downCenter = {-2, -1, 1, 2};
    private final int[] downBottom = {-1, 0, 1};

    private final int[] upBottom = {2, 1, 0, -1, -2};
    private final int[] upCenter = {2, 1, -1, -2};
    private final int[] upTop =  {1, 0, -1};


    public NeighborsTriangle(int row, int col, Cell[][] grid) {
        super(row, col, grid);
    }

    @Override
    protected void setIndexMap() {
        setOrientation();
        int index = -1;


        if(isPointingUp){
            System.out.println("\t Is pointing up");
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
