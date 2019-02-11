cell society
====

This project implements a cellular automata simulator.

Names: Hsing Tang, Irene Qiao, Carrie Hunner

### Timeline

Start Date: Jan.24, 2018

Finish Date: Feb.11, 2018

Hours Spent: 

### Primary Roles
Hsing Tang - Configuration: Created the Simulation, XMLParser and XMLAlert classes. Implemented configuration file parsing, simulation flow control, cell initialization and switching between scenes.

Irene Qiao - Visualization: 

Carrie Hunner - Simulation: Created the cell
classes and the neighbor classes.

### Resources Used


### Running the Program

Main class: Simulation.java

Data files needed:
* English.properties
* SimulationConfig.txt - setting up default parameters and defining simulation models.
* XMLAlertText.txt - providing content of alert messages to display when encountering mal-formatted xml files.
* Specific model configuration files - providing detailed information of cell states and parameters for initializing a simulation process.
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
should be included.
* Allowing for a different variety of grid location shapes:
We currently have functionality to support either square or
triangle cells.
* Allowing for different grid edge types:
Our program currently allows either finite or toroidal edge types.
* Implement additional simulations:
We implemented the RPS simulation.

#Configuration

#Visualization


Assumptions or Simplifications:

Known Bugs:

Extra credit:


### Notes


### Impressions

