package CellSociety.Exceptions;

import CellSociety.SimulationException;

public class SpecErrException extends SimulationException {
    public SpecErrException(Throwable cause){
        super(cause);
    }

    public SpecErrException(String message, Throwable cause){
        super(message,cause);
    }

    public SpecErrException(String message){
        super(message);
    }
}
