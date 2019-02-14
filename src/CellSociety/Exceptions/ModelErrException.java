package CellSociety.Exceptions;

import CellSociety.SimulationException;

/**
 * @author Hsingchih Tang
 * Concrete subclass of SimulationException
 * Expected to be thrown when the simulation model type is missing from model configuration file, or
 * is invalid according to the general simulation configuration file
 */
public class ModelErrException extends SimulationException {

    /**
     * Construct a new ModelErrException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public ModelErrException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new ModelErrException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public ModelErrException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new ModelErrException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public ModelErrException(String message){
        super(message);
    }
}
