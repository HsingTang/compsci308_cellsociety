package CellSociety;

public class CellSpecException extends SimulationException {
    public CellSpecException(Throwable cause){
        super(cause);
    }

    public CellSpecException(String message, Throwable cause){
        super(message,cause);
    }
}
