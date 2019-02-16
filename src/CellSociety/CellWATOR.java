package CellSociety;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Carrie Hunner
 * This is a cell concrete implementation that uses the rules of the WATOR simulation.
 */
public class CellWATOR extends Cell {

    private static final String FISH = "Fish";
    private static final String SHARK = "Shark";
    private static final String EMPTY = "Empty";
    private static final int FISH_REPRO_INDEX = 0;
    private static final int SHARK_REPRO_INDEX = 1;
    private static final int SHARK_ENERGY_INDEX = 2;
    private static final int SHARK_EATING_INDEX = 3;
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

    /**
     * @param row int index of the row of the cell in a grid of cells that will be passed through
     *            when setting neighbors
     * @param col int index of the column of the cell in a grid of cells that will be passed through when]
     *            setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters ArrayList of doubles containing the turns it takes for a fish to reproduce,
     *                   the turns it takes for a shark to reproduce, the initial energy of a shark,
     *                   and the energy a shark gets by eating a fish
     */
    CellWATOR(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);
        myRand = new Random();
        myEmptyNeighbors = new ArrayList<>();
        myFishNeighbors = new ArrayList<>();
        setParams();
        mySharkEnergy = myNewSharkEnergy;
        myTurnsSurvived = 0;
    }

    //sets the parameters of the simulation
    @Override
    protected void setParams(){
        myFishReproTime = myParams.get(FISH_REPRO_INDEX);
        mySharkReproTime = myParams.get(SHARK_REPRO_INDEX);
        myNewSharkEnergy = myParams.get(SHARK_ENERGY_INDEX);
        mySharkEatingEnergy = myParams.get(SHARK_EATING_INDEX);
    }

    //adds all possible states to a list
    @Override
    protected void initializeStatesList() {
        myStates.add(FISH);
        myStates.add(EMPTY);
        myStates.add(SHARK);
    }

    /**
     * Updates the cell's current state.
     */
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
        setParams();
        myFishNeighbors.clear();
        myEmptyNeighbors.clear();
        myTurnsSurvived += 1;
        myNextLocCell = null;
        switch(myCurrentState){
            case FISH:
                //making sure fish wasn't eaten
                if(myNextState.equals("")){
                    findEmptyNeighbors();
                    setFishNextState();
                }
                return;
            case SHARK:
                findFishNeighbors();
                findEmptyNeighbors();
                handleShark();
                return;
            case EMPTY:
                if(myNextState.equals("")){
                    myNextState = EMPTY;
                }
        }
    }

    private void handleShark() {
        if (sharkDied()) return;
        //eating fish
        if(!myFishNeighbors.isEmpty()){
            moveToFishNeighbor();
        }
        else if(!myEmptyNeighbors.isEmpty()){
            moveToEmptyNeighbor();
        }
        else{
            sharkStays();
        }
    }

    private void moveToEmptyNeighbor() {
        checkForBaby(SHARK);
        moveSharkToEmptyNeighbor();
        resetCellIfNecessary();
    }

    private void moveToFishNeighbor() {
        checkForBaby(SHARK);
        eatFish();
        resetCellIfNecessary();
    }

    //checks if a shark died and adjusts its state if it did
    private boolean sharkDied() {
        if(mySharkEnergy <= 0){
            myNextState = EMPTY;
            return true;
        }
        return false;
    }

    //if an animal moves and doesn't have a baby, this resets the cell to be empty
    private void resetCellIfNecessary() {
        //no baby
        if(myNextState.equals("")){
            myNextState = EMPTY;
        }
        myNextTurnsSurvived = 0;
    }

    //handles setting the fish's next state ie if it moves, stays, has a baby
    private void setFishNextState() {
        findEmptyNeighbors();
        if(myEmptyNeighbors.isEmpty()){
            fishStays();
            return;
        }
        else{
            checkForBaby(FISH);
            moveFish();
            resetCellIfNecessary();
            return;
        }
    }

    private void sharkStays() {
        myNextState = SHARK;
        mySharkEnergy--;
    }

    private void moveSharkToEmptyNeighbor() {
        CellWATOR nextCell = findNextLocCell(myEmptyNeighbors);
        nextCell.setNextState(SHARK);
        nextCell.setNextSharkEnergy(mySharkEnergy - 1);
        nextCell.setNextTurnsSurvived(myTurnsSurvived);
    }

    private void eatFish() {
        CellWATOR nextCell = findNextLocCell(myFishNeighbors);
        //make sure if fish had already planned on moving, that cell will be empty instead
        if(nextCell.getNextLocCell() != null){
            nextCell.getNextLocCell().setNextState(EMPTY);
        }
        nextCell.setNextState(SHARK);
        nextCell.setNextSharkEnergy(mySharkEnergy - 1 + mySharkEatingEnergy);
        nextCell.setNextTurnsSurvived(myTurnsSurvived);
    }

    //chooses from a list a random new location
    private CellWATOR findNextLocCell(List<CellWATOR> neighborOptions) {
        int nextLocationIndex;
        nextLocationIndex = myRand.nextInt(neighborOptions.size());
        return neighborOptions.get(nextLocationIndex);
    }

    private void fishStays() {
        myNextState = FISH;
        return;
    }

    /**
     * Takes a cell and returns where it is intending to move to.
     * @return Cell where the animal is intending to move to.
     */
    public CellWATOR getNextLocCell(){
        return myNextLocCell;
    }

    //moves fish to empty spot
    private void moveFish() {
        CellWATOR nextCell = findNextLocCell(myEmptyNeighbors);
        myNextLocCell = nextCell;
        nextCell.setNextState(FISH);
        nextCell.setNextTurnsSurvived(myTurnsSurvived);
    }

    //checks if an animal is qualified to have a baby
    private void checkForBaby(String s){
        switch(s){
            case SHARK:
                if(myTurnsSurvived > mySharkReproTime){
                    setSharkBaby();
                }
                return;
            case FISH:
                if(myTurnsSurvived > myFishReproTime){
                    setFishBaby();
                    return;
                }
                return;
        }
    }

    private void setFishBaby() {
        myNextState = FISH;
        myNextTurnsSurvived = 0;
        myTurnsSurvived = 0;
    }

    private void setSharkBaby() {
        myNextState = SHARK;
        myNextTurnsSurvived = 0;
        myTurnsSurvived = 0;    //reset turns survived
        myNextSharkEnergy = myNewSharkEnergy;
    }

    //finds all neighbors that are fish
    private void findFishNeighbors(){
        for(Cell c: myNeighbors){
            if(c.getState().equals(FISH) && !c.getNextState().equals(SHARK)){
                    myFishNeighbors.add((CellWATOR)c);
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
                    myEmptyNeighbors.add((CellWATOR)c);
                }
            }
        }
    }

    /**
     * This switches the current state to one of the other valid states.
     * Every time this method is called, it keeps track such that it can
     * keep being called and it will cycle through all possible states.
     * This was created for the purpose of UI calling it when a user clicks
     * on a cell.
     */
    @Override
    public void userSwitchState(){
        super.userSwitchState();
        myTurnsSurvived = 0;
        if(myCurrentState.equals(SHARK)){
            mySharkEnergy = myNewSharkEnergy;
        }
    }
}
