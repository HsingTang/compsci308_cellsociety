package CellSociety;

public class CellStateException extends SimulationException {
    public CellStateException(Throwable cause){
        super(cause);
    }

    public CellStateException(String message, Throwable cause){
        super(message,cause);
    }
}
