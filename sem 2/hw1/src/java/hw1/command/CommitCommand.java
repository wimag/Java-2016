package hw1.command;

import hw1.GutUtils;
import hw1.exceptions.MalformedCommandException;
import hw1.exceptions.NoSuchRepositoryException;
import hw1.structure.Commit;
import hw1.structure.StateManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

/**
 * Created by Mark on 25.09.2016.
 * <p>
 * class represents commit command.
 * User should specify commit message
 */
class CommitCommand implements Command {
    private final String message;

    CommitCommand(String[] params) {
        String message1;
        Options options = new Options();
        options.addOption("m", true, "commit message");
        try {
            CommandLine cmd = new DefaultParser().parse(options, params);
            message1 = cmd.getOptionValue("m");
        } catch (ParseException e) {
            message1 = null;
            System.out.println("Couldn't parse command");
        }

        message = message1;
    }

    @Override
    public void execute() throws MalformedCommandException {
        if (message == null) {
            throw new MalformedCommandException("No commit message provided");
        }
        if (!GutUtils.repoInitialized()) {
            throw new NoSuchRepositoryException();
        }
        try {
            StateManager stateManager = StateManager.load();
            String commitId = stateManager.commit(message);
            stateManager.close();
            System.out.println("Added new commit. Current head is " + commitId + "\n");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}