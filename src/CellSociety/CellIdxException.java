package CellSociety;

public class CellIdxException extends SimulationException {
    public CellIdxException(Throwable cause){
        super(cause);
    }

    public CellIdxException(String message, Throwable cause){
        super(message,cause);
    }
}
