package CellSociety.Exceptions;

import CellSociety.SimulationException;

/**
 * @author Hsingchih Tang
 * Concrete subclass of SimulationException
 * Expected to be thrown when the number of parameters provided in a model configuration file does not match
 * the expected number defined in general simulation configuration file for the model
 */
public class ParamErrException extends SimulationException {

    /**
     * Construct a new ParamErrException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public ParamErrException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new ParamErrException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public ParamErrException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new ParamErrException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public ParamErrException(String message){
        super(message);
    }
}

