package CellSociety.Exceptions;

import CellSociety.SimulationException;

public class GridErrException extends SimulationException {
    public GridErrException(Throwable cause){
        super(cause);
    }

    public GridErrException(String message, Throwable cause){
        super(message,cause);
    }

    public GridErrException(String message){
        super(message);
    }
}
