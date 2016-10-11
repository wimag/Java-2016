package hw1.exceptions;

/**
 * Created by Mark on 22.09.2016.
 * <p>
 * Raised if no such branch found found
 */
public class NoSuchBranchException extends MalformedCommandException {
    public NoSuchBranchException() {
        super("Can not find branch with given name");
    }
}
