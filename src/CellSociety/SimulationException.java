package CellSociety;

/**
 * @author Robert C. Duvall
 * @author Rhondu Smithwick
 * @author Hsingchih Tang
 * Abstract class for handling XML configuration file's missing info or mal-formatting issues
 * Has concrete subclasses for different specific cases
 */
public abstract class SimulationException extends RuntimeException {
    public SimulationException(Throwable cause){
        super(cause);
    }

    public SimulationException(String message, Throwable cause){
        super(message,cause);
    }

    public SimulationException(String message){
        super(message);
    }

}
