package CellSociety;

public class ConfigErrException extends SimulationException {
    public ConfigErrException(Throwable cause){
        super(cause);
    }

    public ConfigErrException(String message, Throwable cause){
        super(message,cause);
    }
}
