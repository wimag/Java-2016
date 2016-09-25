package hw1.command;

import hw1.GutUtils;
import hw1.exceptions.MalformedCommandException;
import hw1.exceptions.NoSuchRepositoryException;
import hw1.structure.StateManager;

import java.io.IOException;

/**
 * Created by Mark on 25.09.2016.
 * <p>
 * represents merge command
 */
class MergeCommand implements Command {
    private final String branch;

    public MergeCommand(String[] params) {
        if (params.length != 1) {
            branch = null;
        } else {
            branch = params[0];
        }
    }

    @Override
    public void execute() throws MalformedCommandException {
        if (!GutUtils.repoInitialized()) {
            throw new NoSuchRepositoryException();
        }
        if (branch == null) {
            throw new MalformedCommandException("merge command expects exactly one argument");
        }
        try {
            StateManager stateManager = StateManager.load();
            stateManager.mergeBranch(branch);
            stateManager.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
