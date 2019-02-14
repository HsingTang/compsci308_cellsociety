package CellSociety.Exceptions;

import CellSociety.SimulationException;

public class ParamErrException extends SimulationException {
    public ParamErrException(Throwable cause){
        super(cause);
    }

    public ParamErrException(String message, Throwable cause){
        super(message,cause);
    }

    public ParamErrException(String message){
        super(message);
    }
}

