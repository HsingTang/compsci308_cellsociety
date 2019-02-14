package CellSociety.Exceptions;

import CellSociety.SimulationException;


/**
 * @author Hsingchih Tang
 * Concrete subclass of SimulationException
 * Expected to be thrown when the configuration detail of a cell's initial state is missing
 */
public class CellInfoException extends SimulationException {

    /**
     * Construct a new CellInfoException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public CellInfoException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new CellInfoException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public CellInfoException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new CellInfoException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public CellInfoException(String message){
        super(message);
    }
}
