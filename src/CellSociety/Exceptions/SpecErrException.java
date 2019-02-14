package CellSociety.Exceptions;

import CellSociety.SimulationException;

/**
 * @author Hsingchih Tang
 * Concrete subclass of SimulationException
 * Expected to be thrown when the file parsing pattern information (whether cell initial states are
 * specified in file or to be randomly generated) is not specified or invalid
 */
public class SpecErrException extends SimulationException {

    /**
     * Construct a new SpecErrException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public SpecErrException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new SpecErrException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public SpecErrException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new SpecErrException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public SpecErrException(String message){
        super(message);
    }
}
