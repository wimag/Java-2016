package hw1.exceptions;

/**
 * Created by Mark on 05.10.2016.
 *
 * represents cases, when untracked files are mistakenly accessed
 */
public class UntrackedFileException extends MalformedCommandException {
    public UntrackedFileException(){
        super("Tried to access untracked file");
    }
}
