package hw1.exceptions;

/**
 * Created by Mark on 25.09.2016.
 */
public class DuplicateBranchException extends MalformedCommandException {
    public DuplicateBranchException() {
        super("branch already exists");
    }
}
