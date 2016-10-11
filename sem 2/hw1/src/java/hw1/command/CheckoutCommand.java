package hw1.command;

import hw1.GutUtils;
import hw1.exceptions.MalformedCommandException;
import hw1.exceptions.NoSuchRepositoryException;
import hw1.structure.StateManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

/**
 * Created by Mark on 25.09.2016.
 */
class CheckoutCommand implements Command {
    private final String branchName;
    private final String commitId;

    CheckoutCommand(String[] params) {
        String branchName1;
        String commitId1;
        Options options = new Options();
        options.addOption("b", true, "branch name");
        options.addOption("c", true, "commit name");
        try {
            CommandLine cmd = new DefaultParser().parse(options, params);
            branchName1 = cmd.getOptionValue("b");
            commitId1 = cmd.getOptionValue("c");
        } catch (ParseException e) {
            branchName1 = null;
            commitId1 = null;
            System.out.println("Couldn't parse command");
        }
        commitId = commitId1;
        branchName = branchName1;
    }

    @Override
    public void execute() throws MalformedCommandException {
        if (!GutUtils.repoInitialized()) {
            throw new NoSuchRepositoryException();
        }
        if ((commitId == null) == (branchName == null)) {
            throw new MalformedCommandException("Specify eithert branch or commit");
        }
        try {
            StateManager stateManager = StateManager.load();
            if (commitId != null) {
                stateManager.checkoutCommit(commitId);
            } else {
                stateManager.checkoutBranch(branchName);
            }
            stateManager.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
