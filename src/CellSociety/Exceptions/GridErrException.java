package CellSociety.Exceptions;

import CellSociety.SimulationException;

/**
 * @author Hsingchih Tang
 * Concrete subclass of SimulationException
 * Expected to be thrown when the grid width/height information is missing from model configuration file
 */
public class GridErrException extends SimulationException {

    /**
     * Construct a new GridErrException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public GridErrException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new GridErrException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public GridErrException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new GridErrException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public GridErrException(String message){
        super(message);
    }
}
