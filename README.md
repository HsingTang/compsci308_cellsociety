cell society
====

This project implements a cellular automata simulator.

Names: Hsing Tang, Irene Qiao, Carrie Hunner

### Timeline

Start Date: Jan.24, 2018

Finish Date: Feb.11, 2018

Hours Spent: **I'm thinking whoever fills out there hours last can add them all up**
First meeting (in class) ~ 2 hrs
Planning meeting ~ 3 hours
Putting it together the first time meeting ~3 hrs
Putting it together this past sunday ~2 hrs? you guys may have stayed longer

Outside of meetings:
Carrie: ~20 (i think)

### Primary Roles
Hsing Tang - Configuration: Created the Simulation, XMLParser and 
XMLAlert classes. Implemented configuration file parsing, simulation 
flow control, cell initialization and switching between scenes.

Irene Qiao - Visualization: 

Carrie Hunner - Simulation: Created abstract cell class, 
 the concrete implementations for each simulation, 
 as well as the neighbor abstract class and its concrete implementations.

### Resources Used
[stack overflow](https://stackoverflow.com/)

[segregation rules](https://www2.cs.duke.edu/courses/spring19/compsci308/assign/02_cellsociety/nifty/mccown-schelling-model-segregation/)  
[Wa-Tor rules](https://www2.cs.duke.edu/courses/spring19/compsci308/assign/02_cellsociety/nifty/scott-wator-world/)  
[spreading fire rules](https://www2.cs.duke.edu/courses/spring19/compsci308/assign/02_cellsociety/nifty/shiflet-fire/)  
[percolation rules](https://www2.cs.duke.edu/courses/spring19/compsci308/assign/02_cellsociety/PercolationCA.pdf)  
[game of life rules](https://en.wikipedia.org/wiki/Conway's_Game_of_Life)  
[rocks paper scissors rules](https://www.gamedev.net/blogs/entry/2249737-another-cellular-automaton-video/)  

### Running the Program

Main class: Simulation.java

Data files needed:
* English.properties
* SimulationConfig.txt - setting up default parameters 
and defining simulation models.
* XMLAlertText.txt - providing content of alert messages 
to display when encountering mal-formatted xml files.
* Specific model configuration files - providing detailed information 
of cell states and parameters for initializing a simulation process.
    * Fire.xml
    * Game of Life.xml
    * Percolation.xml
    * RPS.xml
    * Segregation.xml
    * WaTor.xml
    
Interesting data files:

# Features implemented:
## Simulation
* Allowing for a different number of neighbor rearrangements:
The XML file can contain a list of integers that act
as indices to indicate which possible neighbors for a cell
should be included. The list of integers is stored in the 
Simulation class after parsing the xml file, and is passed into 
the cell's findNeighbors() method for generating neighbors.
* Allowing for a different variety of grid location shapes:
We currently have functionality to support either square or
triangle cells. The Neighbors superclass is easy to extend
and add other subclasses to accommodate more shapes in the future.
* Allowing for different grid edge types:
Our program currently allows either finite or toroidal edge types.
Adding another new edge type would also be doable, needing only
a new method within the abstract Neighbors class to find the
grid coordinates of all possible neighbors when an edge is reached.
* Implement additional simulations:
In addition to the five simulations (Fire, Game of Life, Percolation, 
Segregation and WaTor) implemented in the first sprint, we further 
implemented a new RPS simulation. 

##Configuration
* Implement error checking for incorrect file data:
    * Pop up alert dialogue boxes when mal-formatted xml configuration
     files are loaded (e.g. missing configuration information, out-of-bound 
     cell index given, invalid model type, etc.). Alert messages for different 
     mal-formatting issues differ and are loaded from XMLAlertText.txt file at 
     the initialization of the XMLParser.
    * Handle FileNotFoundException, SAXException and ParserConfigurationException
    by throwing the exception to the next-level method, printing error message to 
    console, and eventually terminating the whole program by calling Platform.exit().
    * Set default cell shape to Square and edge type to Toroidal when the values are 
    not specified in a xml configuration file.
* Different simulation initial configuration style:
    * The initial states of cells are assigned completely randomly if no information 
    is specified about each state's distribution percentage or each cell's specific 
    initial state.
    * Cell initial states are assigned based on distribution (accuracy to 0.01) when 
    the percentage distribution of each state is specified.
    * If all above information is missing, and that the file explicitly specifies the 
    initial state of each cell by row and column indices, the XMLParser will read in 
    the data, which will be passed to the Simulation class, where initial state of each 
    cell is assigned based on location indices.
    

##Visualization


###Assumptions or Simplifications:
* **Burning Simulation:** a cell will check if it should catch on fire
once for every burning neighbor it has. This means that more burning neighbors
increases the chance of a tree catching. This was decided because
in a real forest fire situation, more fire nearby would also increase
the likelihood of catching.
* **WATOR Simulation:** It was assumed that each cell could only
house a maximum of one animal per step. This mimics reality, as
two things cannot exist in the same space. Additionally, had more than one
animal been allowed, it would become exceedingly difficult to
keep track of all who was housed in a cell and how to handle
the movements of each inhabitant. Thus, for simplicity and attempting
to remain realistic, cells can only host one animal.
* **Segregation:** The rules for the movement of a cell, when unsatisfied,
were left rather vague. As a result, it was decided that when a cell
became dissatisfied, it would scroll from its starting location
down each row until it either found an empty cell or looped all the
way to its starting coordinates. If it made it back to where it began,
the cell would not move, as there were no empty spaces that hadn't been
claimed by other cells. This was a fairly easy way to ensure that
the cell would only check each other cell once, as opposed to being completely
random. While this does result in the first few steps displaying
extremely large groups moving to the bottom and then the top of the grid,
it quickly disperses and still results in a successful simulation.
* **Rocks Paper Scissors:** If, when a cell is checked to set its next
state, the next state has already been set, it is assumed that
it was "eaten" by a neighbor and it is not able to affect its neighbors
in this step. Also, white is considered to be able to be "eaten" by any other cell,
whether white tries to "eat" another color or is attempted to be "eaten"
by another color, it always loses. Lastly, the "gradient" component
of the simulation was not added due to time constraints, however
it would be easy to add in the future. Within the CellRPS class
a gradient variable would need to be created and incremented and decremented
as appropriate, and then the XML file would need to add a parameter 
and the UI would need to add a parameter slider. All of these are
extremely doable with our current structure.

Known Bugs:

Extra credit:


### Notes
**Wator Simulation:** A shark is checked for death at the beginning
of a step, before attempting to move or eat.
 This can result in a shark dying with a fish next to it
if its energy is too low. This may look like a glitch, or sometimes
like a fish is eating a shark, but it is not.

### Impressions
We spent a large amount of time planning for sprint 1 to make
our code flexible and to try and anticipate what new features
could be requested. As a result, most of the additions were
relatively easy to add. 

For simulation, some refactoring was required
to better accommodate variables that affected the cell's neighbors 
(ie edgetype and specifying specific neighbors) but most of the code
was written and just needed to be separated and organized. Additionally,
adding the RPS simulation was relatively easy, as it only
required extending the super class and then writing the 
findNextState method.
