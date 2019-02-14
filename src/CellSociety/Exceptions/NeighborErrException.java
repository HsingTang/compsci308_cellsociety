package CellSociety.Exceptions;

import CellSociety.SimulationException;

public class NeighborErrException extends SimulationException {
    public NeighborErrException(Throwable cause){
        super(cause);
    }

    public NeighborErrException(String message, Throwable cause){
        super(message,cause);
    }

    public NeighborErrException(String message){
        super(message);
    }
}
