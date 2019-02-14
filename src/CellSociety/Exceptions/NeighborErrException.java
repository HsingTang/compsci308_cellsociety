package CellSociety.Exceptions;

import CellSociety.SimulationException;

/**
 * @author Hsingchih Tang
 * Concrete subclass of SimulationException
 * Expected to be thrown when the cell neighbor indices provided in
 * the model configuration file is invalid (out of bound) or missing
 */
public class NeighborErrException extends SimulationException {

    /**
     * Construct a new NeighborErrException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public NeighborErrException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new NeighborErrException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public NeighborErrException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new NeighborErrException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public NeighborErrException(String message){
        super(message);
    }
}
