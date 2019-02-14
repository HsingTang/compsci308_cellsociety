package CellSociety.Exceptions;

import CellSociety.SimulationException;

public class ModelErrException extends SimulationException {
    public ModelErrException(Throwable cause){
        super(cause);
    }

    public ModelErrException(String message, Throwable cause){
        super(message,cause);
    }

    public ModelErrException(String message){
        super(message);
    }
}
