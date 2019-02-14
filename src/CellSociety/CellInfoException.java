package CellSociety;

public class CellInfoException extends SimulationException{
    public CellInfoException(Throwable cause){
        super(cause);
    }

    public CellInfoException(String message, Throwable cause){
        super(message,cause);
    }
}
