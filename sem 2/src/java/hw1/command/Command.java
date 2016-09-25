package hw1.command;

import hw1.exceptions.MalformedCommandException;

/**
 * Created by Mark on 24.09.2016.
 * <p>
 * This interface represents command that are allowed to be executed.
 * Each command may have it's own argument parser
 */
public interface Command {
    void execute() throws MalformedCommandException;
}
