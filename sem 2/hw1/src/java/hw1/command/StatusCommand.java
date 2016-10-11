package hw1.command;

import hw1.GutUtils;
import hw1.exceptions.MalformedCommandException;
import hw1.exceptions.NoSuchRepositoryException;
import hw1.structure.StateManager;

import java.io.IOException;

/**
 * Created by Mark on 05.10.2016.
 */
public class StatusCommand implements Command {
    @Override
    public void execute() throws MalformedCommandException {
        if (!GutUtils.repoInitialized()) {
            throw new NoSuchRepositoryException();
        }
        try {
            StateManager stateManager = StateManager.load();
            StringBuilder builder = new StringBuilder();
            builder.append(" Files changed: \n");
            stateManager.getChangedFiles().forEach(x -> builder.append(x).append("\n"));
            builder.append("\n\n Files removed: \n");
            stateManager.getDeletedFiles().forEach(x -> builder.append(x).append("\n"));
            builder.append("\n\n Untracked files: \n");
            stateManager.getUntrackedFiles().forEach(x -> builder.append(x).append("\n"));
            System.out.println(builder.toString());
            stateManager.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
