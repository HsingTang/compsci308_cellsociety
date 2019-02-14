package CellSociety;

public class GridErrException extends SimulationException {
    public GridErrException(Throwable cause){
        super(cause);
    }

    public GridErrException(String message, Throwable cause){
        super(message,cause);
    }
}
