DESIGN
===============
# Design Goals
* 


# Add New Features:
## Adding another simulation:
* **Cell Class:** Our project has an abstract cell superclass that can be extended to create a new simulation. The superclass contains the majority of the necessary methods such that there are only three methods that have to be written. The core method is findNextState(). This is where the rules of the simulation come into play. The end result of this method is determining how the cell should respond, given its current neighbors. The myNextState variable must be assigned somewhere within this method. Additionally, the setParams() method needs to be written to assign all the parameters passed through the constructor to the appropriate variables. This allows the UI to adjust the parameters and cell regularly calls this method to ensure that they are updated. Lastly, the initializeStatesList() needs to be written. This is just creating a list of all possible states for the simulation.
* 
## Adding a new Cell Shape
* **Neighbors:** There is an abstract Neighbors superclass that can be extended. Once extended, only one method needs to be written. The setIndexMap() creates a map with the agreed upon index for a current neighbor as a key. The value would then be a list with the change in row and then the change in column. For example, look at the image below. Here, for each cell neighbor, the larger number is the index, used as the key in the map, and the coordinates are the change in row and column from the original cell, stored in a list in the value of the map.
![](https://i.imgur.com/nIltupv.png)
 By creating this hashmap, the superclass then has defined methods that allow the cell simulation class to choose which neighbors are active and the cell should take into consideration.
 The new shape will also need to be added in the cell class in a switch case such that the correct neighborhood will be created.

*  

## Adding a New Edge Type:
* Neighbors: This is currently handled in the Neighbors superclass in the handleEdgesAndAddCoords() method. When a cell is setting its neighbors and one of its neighbors would be out of bounds, it calls this method to determine if and how that neighbor should be added. Within this method, the edge type is checked and a corresponding method is called. For example, if the edge type is toroidal, a method is called find the correct coordinates to the correct neighbor on the other side of the grid. To add a new edge type, currently it would be necessary to write a method to determine the coordinates of the correct cell and then call that method within the handleEdgesAndAddCoords() method.

# Major Design Choices
* **Abstract Superclass for Cell:** A lot of the methods for the cell class are the same, such as: findNeighbors(), updateState(), userSwitchState(), and several others. By creating an abstract superclass, these methods are shared with all its children and it avoids duplication, it's easy to extend and add new simulations, and it separates the code in a manner that is readable.
* **Abstract Superclass for Neighbor:** This was a harder decison. The benefits were, that regardless of the shape, there are several methods that would be the same. However, in each concrete implementation, there is only one method that is actually written. This made it hard to justify the necessity for a new class for each shape. However, no other option that wouldn't involve large amounts of duplicate code or adding the methods directly to the cell class were thought of, and so an abstract superclass was made. This worked well and makes it easy to add new shapes in terms of locating a cell's neighbors.


# Assumptions or decision
* **Burning Simulation:** a cell will check if it should catch on fire once for every burning neighbor it has. This means that more burning neighbors increases the chance of a tree catching. This was decided because in a real forest fire situation, more fire nearby would also increase the likelihood of catching.
* **WATOR Simulation:** It was assumed that each cell could only house a maximum of one animal per step. This mimics reality, as two things cannot exist in the same space. Additionally, had more than one animal been allowed, it would become exceedingly difficult to keep track of all who was housed in a cell and how to handle the movements of each inhabitant. Thus, for simplicity and attempting to remain realistic, cells can only host one animal.
* **Segregation:** The rules for the movement of a cell, when unsatisfied, were left rather vague. As a result, it was decided that when a cell became dissatisfied, it would scroll from its starting location down each row until it either found an empty cell or looped all the way to its starting coordinates. If it made it back to where it began, the cell would not move, as there were no empty spaces that hadn't been claimed by other cells. This was a fairly easy way to ensure that the cell would only check each other cell once, as opposed to being completely random. While this does result in the first few steps displaying extremely large groups moving to the bottom and then the top of the grid, it quickly disperses and still results in a successful simulation.
* **Rocks Paper Scissors:** If, when a cell is checked to set its next state, the next state has already been set, it is assumed that it was "eaten" by a neighbor and it is not able to affect its neighbors in this step. Also, white is considered to be able to be "eaten" by any other cell, whether white tries to "eat" another color or is attempted to be "eaten" by another color, it always loses. Lastly, the "gradient" component of the simulation was not added due to time constraints, however it would be easy to add in the future. Within the CellRPS class a gradient variable would need to be created and incremented and decremented as appropriate, and then the XML file would need to add a parameter and the UI would need to add a parameter slider. All of these are extremely doable with our current structure.
