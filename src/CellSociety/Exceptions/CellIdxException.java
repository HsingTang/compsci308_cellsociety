package CellSociety.Exceptions;

import CellSociety.SimulationException;

/**
 * @author Hsingchih Tang
 * Concrete subclass of SimulationException
 * Expected to be thrown when the parsed index of a cell is out of grid bounds
 */
public class CellIdxException extends SimulationException {

    /**
     * Construct a new CellIdxException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public CellIdxException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new CellIdxException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public CellIdxException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new CellIdxException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public CellIdxException(String message){
        super(message);
    }
}
