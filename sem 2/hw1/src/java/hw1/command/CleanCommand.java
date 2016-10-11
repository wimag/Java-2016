package hw1.command;

import hw1.GutUtils;
import hw1.exceptions.MalformedCommandException;
import hw1.exceptions.NoSuchRepositoryException;
import hw1.structure.StateManager;

import java.io.IOException;

/**
 * Created by Mark on 05.10.2016.
 */
public class CleanCommand implements Command {
    @Override
    public void execute() throws MalformedCommandException {
        if (!GutUtils.repoInitialized()) {
            throw new NoSuchRepositoryException();
        }
        try {
            StateManager stateManager = StateManager.load();
            stateManager.cleanProjectFiles();
            stateManager.close();
            System.out.println("Repository cleaned");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
