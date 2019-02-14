package CellSociety.Exceptions;

import CellSociety.SimulationException;

/**
 * @author Hsingchih Tang
 * Concrete subclass of SimulationException
 * Expected to be thrown when the specification information is missing, or when the previously parsed data indicates
 * that cell states shall be explicitly defined in file, but no cell initial state information is provided
 */
public class CellSpecException extends SimulationException {

    /**
     * Construct a new CellSpecException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public CellSpecException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new CellSpecException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public CellSpecException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new CellSpecException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public CellSpecException(String message){
        super(message);
    }
}
