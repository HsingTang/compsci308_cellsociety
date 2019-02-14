package CellSociety.Exceptions;

import CellSociety.SimulationException;

public class CellInfoException extends SimulationException {
    public CellInfoException(Throwable cause){
        super(cause);
    }

    public CellInfoException(String message, Throwable cause){
        super(message,cause);
    }

    public CellInfoException(String message){
        super(message);
    }
}
