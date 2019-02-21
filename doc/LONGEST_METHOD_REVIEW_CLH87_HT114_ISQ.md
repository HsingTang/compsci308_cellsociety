LONGEST_METHOD_REVIEW
===
##### ht114, clh87, isq
### Simulation.initGrid()
1. Design Issues
    * **Long switch-case structures (Lines 162-181)**
        The method adopts the switch-case structure to initialize grid of cells by invoking different concrete cell classes based on the SIM_TYPE field, which is the longest portion of codes in this method.
        
    * **Messy initialization pipeline control (Lines 185-187)**
        During initialization for a new simulation model, Simulation is expected to go through a pipeline of methods for setting up different features, which doesn't necessarily have to be handled in initGrid().
        
    * **Less relevant functions (Lines 146-154)**
        The exception handling and file parsing call can be processed in the other method instead of being incorporated into initGrid().

2. Organized Design Issues
    * Creating new CellRefactory classes for handling concrete cell class invoking logics:
        * Long switch-case structure
    * Migrating (re-organizing) function sequences in other methods within Simulation class:
        * Messy pipeline control
        * Less-relevant functions

3. Refactoring
    * **Long switch-case structure**
        A new class (say "CellFactory") can be created separately, which will handle the concrete cell class invoking logics independently outside of the Simulation class. CellFactory will take in arguments from Simulation through a call on CellFactory.makeCell(i,j,cellState,params,SIM_TYPE), create the corresponding cell object based on SIM_TYPE and return the cell of correct type. The switch-case structure will be migrated to CellFactory, which will effectively reduce the length of initGrid() function.
    * **Pipeline control**
        Simulation can have a function initPipeline() which sequentially calls all init-methods (readXML(), initGrid(), initNeighbors(), initUI() ... etc.) for setting up all different components. This approach would free initGrid() from the responsibility of invoking its subsequent methods, and could also provide easier control on the pipeline flow.
    * **Less-relevant functions**
        Similar to the approach described above, the initPipeline() method could make the call on readXML() and handle the exception outside of initGrid().
### CellRPS.compare(String s1, String s2)
1. List as many design issues as you can in the method (using line numbers to identify the exact code) from large things like (potential) duplicated code or if statements that should be generalized through polymorphism, data structures, or resource files down to medium sized things like poor error handling or long lambdas methods to small things like consistent coding conventions or ignored assignment design requirements (like using Resources instead of magic values). For many of these methods, this should be a long list of issues!
    * **Embedded If Statements (Lines 115-145):**
    This method is a comparator and to determine what value (-1, 0, 1) to return, several if statements were used.
        
    * **Similar/Duplicated Code (Lines 124-128, 131-136, 140-144):**
    Within each if statement, there are more if statements whose structure is extremely similar and bordeline duplicate.

2. Organize the list of issues based on things that could be fixed together based on a different design choice or using similar refactorings and prioritize these groups based on which would provide the most improvement to the overall code (not just this method).
    * **Embedded If Statements:** 
    These lengthen the method significantly and make it harder to read. Additionally, it would be difficult to add a fourth color option if so desired.
    * **Similar/Duplicated Code:**
    These are short lines, however they are extremly similar and occur three times in the method. Adjusting them and extracting them would make the method cleaner and easier to read.

3. Describe specific overall design changes or refactorings to fix the three most important issues you identified.
    * **Embedded If Statements:**
    This method could be largely improved by creating a HashMap where the key is the state of the s1 and the value is an ArrayList of states that the key is able to eat. It would then be easy to check if HashMap.get(s1).contains(s2). If it did, then the method could return 1 because s1 can eat s2. If it did not, it could return -1, indicating that s1 is eaten. Before this is done, it would be necessary to check if s1.equals(s2) and return 0. This would eliminate the embedded if tree.
    * **Similar/Duplicated Code:**
    The design described above would also solve the problem of duplicated code. Instead of checking if s1.equals() every possible state, the HashMap would need to be checked once. This would shorten the method as well as enhance readability and extensibility. Adding another color would be as simple as adding it to the HashMap and defining which states it could eat.

### UI.initStartingCoordinates(String shape)
1. List as many design issues as you can in the method (using line numbers to identify the exact code) from large things like (potential) duplicated code or if statements that should be generalized through polymorphism, data structures, or resource files down to medium sized things like poor error handling or long lambdas methods to small things like consistent coding conventions or ignored assignment design requirements (like using Resources instead of magic values). For many of these methods, this should be a long list of issues!
    * **Long, hard-coded variables, lines 140 - 169**
        Depending on the shape, there were different starting coordinates and different variable values. These starting coordinates and variable assignments are hard-coded and take up a lot of space - especially because there are many coordinate values to assign. 
        
    * Placeholder 2 

2. Organize the list of issues based on things that could be fixed together based on a different design choice or using similar refactorings and prioritize these groups based on which would provide the most improvement to the overall code (not just this method).
    * **Long, hard-coded variables, lines 140 - 169**

3. Describe specific overall design changes or refactorings to fix the three most important issues you identified.
    * Creating a CellShape class that would 1. encapsulate shape initialization information and 2. allow more flexibility in implementing new cell shapes
    * Each CellShape subclass would contain variable assignments and methods to calculate cell coordinates given current cell location
    * The initStartingCoordinates class would not be necessary anymore - only need to add simple switch case to initCellVisMap that constructs the appropriate CellShape