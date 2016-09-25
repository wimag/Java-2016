package hw1.exceptions;

/**
 * Created by Mark on 22.09.2016.
 * <p>
 * raised at attampt of rewriting existing repository
 */
public class RepositoryOverrideException extends MalformedCommandException {
    public RepositoryOverrideException() {
        super("Can not create a repository in the same directory twice");
    }
}
