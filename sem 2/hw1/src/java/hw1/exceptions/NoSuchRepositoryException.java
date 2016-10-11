package hw1.exceptions;

/**
 * Created by Mark on 22.09.2016.
 * <p>
 * Raised if no repository found
 */
public class NoSuchRepositoryException extends MalformedCommandException {
    public NoSuchRepositoryException() {
        super("No repository was initialized in current folder");
    }
}
