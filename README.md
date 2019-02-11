cell society
====

This project implements a cellular automata simulator.

Names: Hsing Tang, Irene Qiao, Carrie Hunner

### Timeline

Start Date: Jan.24, 2018

Finish Date: Feb.11, 2018

Hours Spent: 

### Primary Roles
Hsing Tang - Configuration: Created the Simulation, XMLParser and 
XMLAlert classes. Implemented configuration file parsing, simulation 
flow control, cell initialization and switching between scenes.

Irene Qiao - Visualization: 

Carrie Hunner - Simulation: Created the cell
classes and the neighbor classes.

### Resources Used


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

Features implemented:

# Simulation
* Allowing for a different number of neighbor rearrangements:
The XML file can contain a list of integers that act
as indices to indicate which possible neighbors for a cell
should be included. The list of integers is stored in the 
Simulation class after parsing the xml file, and is passed into 
the cell's findNeighbors() method for generating neighbors.
* Allowing for a different variety of grid location shapes:
We currently have functionality to support either square or
triangle cells.
* Allowing for different grid edge types:
Our program currently allows either finite or toroidal edge types.
* Implement additional simulations:
In addition to the five simulations (Fire, Game of Life, Percolation, 
Segregation and WaTor) implemented in the first sprint, we further 
implemented a new RPS simulation.

#Configuration
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
    

#Visualization


Assumptions or Simplifications:

Known Bugs:

Extra credit:


### Notes


### Impressions

