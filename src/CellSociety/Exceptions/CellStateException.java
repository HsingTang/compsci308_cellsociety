package CellSociety.Exceptions;

import CellSociety.SimulationException;

/**
 * @author Hsingchih Tang
 * Concrete subclass of SimulationException
 * Expected to be thrown when the initial state information of a cell is missing from model configuration file
 */
public class CellStateException extends SimulationException {

    /**
     * Construct a new CellStateException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public CellStateException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new CellStateException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public CellStateException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new CellStateException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public CellStateException(String message){
        super(message);
    }
}
