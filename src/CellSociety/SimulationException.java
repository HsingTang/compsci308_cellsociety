package CellSociety;

public abstract class SimulationException extends RuntimeException {
    public SimulationException(Throwable cause){
        super(cause);
    }

    public SimulationException(String message, Throwable cause){
        super(message,cause);
    }

}
