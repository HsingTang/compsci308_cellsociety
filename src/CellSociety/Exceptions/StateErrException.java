package CellSociety.Exceptions;

import CellSociety.SimulationException;

/**
 * @author Hsingchih Tang
 * Concrete subclass of SimulationException
 * Expected to be thrown when the model's state configuration information (state name, image, etc.) is missing
 * or invalid according to the definition in general simulation configuration file for the model
 */
public class StateErrException extends SimulationException {

    /**
     * Construct a new StateErrException by wrapping another Throwable
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public StateErrException(Throwable cause){
        super(cause);
    }


    /**
     * Construct a new StateErrException by wrapping another Throwable and with the message argument
     * @param message the detail message associated with the new SimulationException
     * @param cause the Throwable to be wrapped as a SimulationException
     */
    public StateErrException(String message, Throwable cause){
        super(message,cause);
    }


    /**
     * Construct a new StateErrException with the message argument
     * @param message the detail message associated with the new SimulationException
     */
    public StateErrException(String message){
        super(message);
    }
}
