package CellSociety;

public class ParamErrException extends SimulationException{
    public ParamErrException(Throwable cause){
        super(cause);
    }

    public ParamErrException(String message, Throwable cause){
        super(message,cause);
    }
}

