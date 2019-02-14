package CellSociety.Exceptions;

import CellSociety.SimulationException;

public class CellIdxException extends SimulationException {
    public CellIdxException(Throwable cause){
        super(cause);
    }

    public CellIdxException(String message, Throwable cause){
        super(message,cause);
    }

    public CellIdxException(String message){
        super(message);
    }
}
