package exceptions;

/**
 * Created by Mark on 11.10.2016.
 */
public class UnknownQueryException extends IllegalArgumentException {
    public UnknownQueryException(String message) {
        super(message);
    }
}
