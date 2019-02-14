package CellSociety.Exceptions;

import CellSociety.SimulationException;

public class CellStateException extends SimulationException {
    public CellStateException(Throwable cause){
        super(cause);
    }

    public CellStateException(String message, Throwable cause){
        super(message,cause);
    }

    public CellStateException(String message){
        super(message);
    }
}
