package CellSociety;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CellWATOR extends Cell {

    private final String FISH = "Fish";
    private final String SHARK = "Shark";
    private final String EMPTY = "Empty";

    private double myFishReproTime;
    private double mySharkEnergy;
    private double myNewSharkEnergy;
    private double mySharkEatingEnergy;
    private double mySharkReproTime;

    private double myNextTurnsSurvived;
    private double myNextSharkEnergy;

    private double myTurnsSurvived;
    private Random myRand;
    private List<CellWATOR> myEmptyNeighbors;
    private List<CellWATOR> myFishNeighbors;
    private CellWATOR myNextLocCell;

    CellWATOR(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);

        myRand = new Random();
        myEmptyNeighbors = new ArrayList<>();
        myFishNeighbors = new ArrayList<>();

        myFishReproTime = parameters.get(0);
        mySharkReproTime = parameters.get(1);
        mySharkEnergy = parameters.get(2);
        myNewSharkEnergy = parameters.get(2);
        mySharkEatingEnergy = parameters.get(3);


        myTurnsSurvived = 0;
    }

    @Override
    protected void initializeStatesList() {
        myStates.add(FISH);
        myStates.add(EMPTY);
        myStates.add(SHARK);
    }

    @Override
    public void updateState(){
        myCurrentState = myNextState;
        myNextState = "";

        myTurnsSurvived = myNextTurnsSurvived;
        mySharkEnergy = myNextSharkEnergy;

        myNextTurnsSurvived = 0;
        myNextSharkEnergy = 0;
    }
    /**
     * Finds and sets the next state of the cell by checking neighbors
     * and following the rules set.
     */
    @Override
    public void findNextState() {
        myFishNeighbors.clear();
        myEmptyNeighbors.clear();

        myTurnsSurvived += 1;
        myNextLocCell = null;

        switch(myCurrentState){
            case FISH:
                //making sure fish wasn't eaten
                //System.out.println(myCurrentState + " at Row: " + myRow + " Col: " + myCol);
                //System.out.println("\t Turns Survived: " + myTurnsSurvived);
                ////System.out.println("\t Above Next State: " + myNextState);
                if(myNextState.equals("")){
                    findEmptyNeighbors();
                    ////System.out.println("\t Above has " + myEmptyNeighbors.size() + " Empty Neighbors");

                    //can't move
                    if(myEmptyNeighbors.size() == 0){
                        fishStays();
                        ////System.out.println("\t Above can't move");
                        ////System.out.println("\t\tFinal Next State: " + myNextState);
                        ////System.out.println("\t\tFinal Current State: " + myCurrentState);
                        return;
                    }
                    else{
                        moveFish();
                        ////System.out.println("\t about to check for baby");
                        checkForBaby(FISH);
                        ////System.out.println("\t\tFinal Next State: " + myNextState);
                        ////System.out.println("\t\tFinal Current State: " + myCurrentState);
                        return;
                    }
                }
                else{
                    return;
                }
            case SHARK:
                //System.out.println(myCurrentState + " at Row: " + myRow + " Col: " + myCol);
                //System.out.println("\t Turns Survived: " + myTurnsSurvived);
                //System.out.println("\t Energy: " + mySharkEnergy);

                findFishNeighbors();
                findEmptyNeighbors();
                ////System.out.println("\tAbove has " + myEmptyNeighbors.size() + " Empty Neighbors");
                ////System.out.println("\tAbove has " + myFishNeighbors.size() + " Fish Neighbors");

                //shark died
                if(mySharkEnergy <= 0){
                    myNextState = EMPTY;
                    //System.out.println("\t SHARK DIED");
                    ////System.out.println("\t\tFinal Next State: " + myNextState);
                    ////System.out.println("\t\tFinal Current State: " + myCurrentState);
                    return;
                }

                //eating fish
                if(myFishNeighbors.size() != 0){
                    eatFish();
                    ////System.out.println("\t about to check for baby");
                    checkForBaby(SHARK);
                    ////System.out.println("\t\tFinal Next State: " + myNextState);
                    ////System.out.println("\t\tFinal Current State: " + myCurrentState);
                }
                //no fish to eat
                else{
                    //moves to empty space
                    if(myEmptyNeighbors.size() != 0){
                        moveSharkToEmptyNeighbor();
                        ////System.out.println("\t about to check for baby");
                        checkForBaby(SHARK);
                        ////System.out.println("\t\tFinal Next State: " + myNextState);
                        ////System.out.println("\t\tFinal Current State: " + myCurrentState);
                        return;
                    }
                    //can't move
                    else{
                        sharkStays();
                        ////System.out.println("\t\tFinal Next State: " + myNextState);
                        ////System.out.println("\t\tFinal Current State: " + myCurrentState);
                        return;
                    }
                }

            case EMPTY:
                if(myNextState.equals("")){
                    myNextState = EMPTY;
                }
        }
    }

    private void sharkStays() {
        myNextState = SHARK;
        mySharkEnergy--;
    }

    private void moveSharkToEmptyNeighbor() {
        int nextLocationIndex;
        nextLocationIndex = myRand.nextInt(myEmptyNeighbors.size());
        CellWATOR nextCell = myEmptyNeighbors.get(nextLocationIndex);
        //System.out.println("\tAbove wants to move to empty row: " + nextCell.myRow + " Col: " + nextCell.myCol);
        nextCell.setNextState(SHARK);
        ////System.out.println("\tAbove new state is: " + nextCell.getNextState());
        nextCell.setNextSharkEnergy(mySharkEnergy - 1);
        nextCell.setNextTurnsSurvived(myTurnsSurvived);
    }

    private void eatFish() {
        int nextLocationIndex;
        nextLocationIndex = myRand.nextInt(myFishNeighbors.size());
        CellWATOR nextCell = myFishNeighbors.get(nextLocationIndex);
        //System.out.println("\tAbove going to eat Row: " + nextCell.myRow + " Col: " + nextCell.myCol);
        ////System.out.println("\tShark Energy: " + mySharkEnergy);

        //make sure if fish had already planned on moving, that cell will be empty instead
        if(nextCell.getNextLocCell() != null){
            nextCell.getNextLocCell().setNextState(EMPTY);
        }
        nextCell.setNextState(SHARK);

        nextCell.setNextSharkEnergy(mySharkEnergy - 1 + mySharkEatingEnergy);
        nextCell.setNextTurnsSurvived(myTurnsSurvived);
        myNextState = EMPTY;
        ////System.out.println("\tSet above to EMPTY cuz eating fish");
        ////System.out.println("\tEaten Fish cell state: " + nextCell.getNextState());
    }

    private void fishStays() {
        myNextState = FISH;
        ////System.out.println("\tFish Stayed row: " + myRow + "");
        return;
    }

    public CellWATOR getNextLocCell(){
        return myNextLocCell;
    }

    //moves fish to empty spot
    private void moveFish() {
        int numEmptyNeighbors = myEmptyNeighbors.size();
        int nextLocationIndex = myRand.nextInt(numEmptyNeighbors);
        CellWATOR nextCell = myEmptyNeighbors.get(nextLocationIndex);
        ////System.out.println("\tAbove wants to move to row: " + nextCell.myRow + " Col: " + nextCell.myCol);
        myNextLocCell = nextCell;
        nextCell.setNextState(FISH);
        nextCell.setNextTurnsSurvived(myTurnsSurvived);
    }

    private void checkForBaby(String s){
        switch(myCurrentState){
            case SHARK:
                if(myTurnsSurvived > mySharkReproTime){
                    myNextState = SHARK;
                    //System.out.println("\t Turns: " + myTurnsSurvived + " > Repro " + mySharkReproTime + " = BABY");
                    myNextTurnsSurvived = 0;
                    myTurnsSurvived = 0;    //reset turns survived
                    myNextSharkEnergy = myNewSharkEnergy;
                    return;
                }
                else{
                    myNextState = EMPTY;
                    myTurnsSurvived = 0;
                    //System.out.println("\t no baby");
                    return;
                }
            case FISH:
                if(myTurnsSurvived > myFishReproTime){
                    //System.out.println("\t Turns: " + myTurnsSurvived + " > Repro " + myFishReproTime + " = BABY");
                    myNextTurnsSurvived = 0;
                    myNextState = FISH;
                    myTurnsSurvived = 0;
                    //System.out.println("\t BABY");

                    return;
                }
                else{
                    myTurnsSurvived = 0;
                    myNextState = EMPTY;
                    //System.out.println("\t no baby");
                    return;
                }
        }
    }

    private void findFishNeighbors(){
        for(Cell c: myNeighbors){
            if(c.getState().equals(FISH)){
                //make sure not claimed by other shark
                if(!c.getNextState().equals(SHARK)){
                    myFishNeighbors.add((CellWATOR)c);
                }
                else{
                    ////System.out.println("\tFish neighbor claimed by other shark");
                }
            }
        }
    }

    /**
     * Sets the number of turns survived.
     * This value is used in determining the reproduction of the animal.
     * @param turnsSurvived double of the number of turns survived.
     */
    public void setNextTurnsSurvived(double turnsSurvived){
        myNextTurnsSurvived = turnsSurvived;
    }

    /**
     * Sets the shark's energy.
     * Used in determining if shark dies.
     * @param energy double of the shark's current energy.
     */
    public void setNextSharkEnergy(double energy){
        myNextSharkEnergy = energy;
    }

    //locates and stores empty neighbors
    private void findEmptyNeighbors(){
        for(Cell c: myNeighbors){
            if(c.getState().equals(EMPTY)){
                if(c.getNextState().equals("") || c.getNextState().equals(EMPTY)){
                    ////System.out.println("\t \tNeighbors next state it: " + c.getNextState());
                    myEmptyNeighbors.add((CellWATOR)c);
                }
                else{
                    ////System.out.println("\t \tAbove has a claimed neighbor: " + c.getNextState());
                }

            }
        }
    }


    /**
     * Finds and sets neighbors.
     * Assumes the cell can have up to four neighbors.
     * @param cell 2D int array of Cell objects
     */
    @Override
    public void findNeighbors(Cell[][] cell) {
        myGrid = cell;
        generateFourNeighbors();
    }
}
