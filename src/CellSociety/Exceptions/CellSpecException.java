package CellSociety.Exceptions;

import CellSociety.SimulationException;

public class CellSpecException extends SimulationException {
    public CellSpecException(Throwable cause){
        super(cause);
    }

    public CellSpecException(String message, Throwable cause){
        super(message,cause);
    }

    public CellSpecException(String message){
        super(message);
    }
}
