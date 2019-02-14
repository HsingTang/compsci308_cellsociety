package CellSociety;

/**
 * @author Robert C. Duvall
 * @author Rhondu Smithwick
 * @author Hsingchih Tang
 *
 * Code Refactoring:
 * Newly created abstract class specifically for handling XML configuration file's invalid/missing info
 * or mal-formatting issues. Has several concrete subclasses, each for a specific exceptional case.
 */
public abstract class SimulationException extends RuntimeException {

    /**
     * Construct a new SimulationException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public SimulationException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new SimulationException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public SimulationException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new SimulationException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public SimulationException(String message){
        super(message);
    }

}
