package CellSociety.Exceptions;

import CellSociety.SimulationException;

public class StateErrException extends SimulationException {
    public StateErrException(Throwable cause){
        super(cause);
    }

    public StateErrException(String message, Throwable cause){
        super(message,cause);
    }

    public StateErrException(String message){
        super(message);
    }
}
