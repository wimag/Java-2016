package hw1.exceptions;

/**
 * Created by Mark on 25.09.2016.
 * <p>
 * Exceptions that are thrown in command executions
 */
public class MalformedCommandException extends Exception {
    public MalformedCommandException(String message) {
        super(message);
    }
}
