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
 * <p>
 * class represents branchName creation command
 */

class BranchCommand implements Command {
    private final String branchName;
    private boolean create = false;

    BranchCommand(String[] params) {
        String branchName1;
        Options options = new Options();
        options.addOption("b", true, "create branch name");
        options.addOption("d", false, "delete branch");
        try {
            CommandLine cmd = new DefaultParser().parse(options, params);
            branchName1 = cmd.getOptionValue("b");
            create = !cmd.hasOption("d");
        } catch (ParseException e) {
            branchName1 = null;
            System.out.println("Couldn't parse command");
        }
        branchName = branchName1;
    }

    @Override
    public void execute() throws MalformedCommandException {
        if (branchName == null) {
            throw new MalformedCommandException("No branch name provided");
        }
        if (!GutUtils.repoInitialized()) {
            throw new NoSuchRepositoryException();
        }
        try {
            StateManager stateManager = StateManager.load();
            if (create) {
                stateManager.createBranch(branchName);
            } else {
                if (branchName.equals("Mater")) {
                    System.out.println("Can not delete master branch");
                    return;
                }
                stateManager.deleteBranch(branchName);
            }
            stateManager.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
