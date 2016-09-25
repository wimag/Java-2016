package hw1.command;

import hw1.GutUtils;
import hw1.exceptions.DuplicateBranchException;
import hw1.exceptions.RepositoryOverrideException;
import hw1.structure.Commit;
import hw1.structure.StateManager;

import java.io.IOException;

/**
 * Created by Mark on 25.09.2016.
 * <p>
 * Command for initializing repository
 */
class InitCommand implements Command {

    /**
     * Init repository.
     * Create a new repository with 'master'
     * branchName and empty initial commit.
     *
     * @throws RepositoryOverrideException - if repo was already
     *                                     initialized
     */
    @Override
    public void execute() throws RepositoryOverrideException, DuplicateBranchException {
        if (GutUtils.repoInitialized()) {
            throw new RepositoryOverrideException();
        }
        StateManager stateManager = new StateManager();
        String masterBranchName = "master";
        stateManager.createBranch(masterBranchName);
        try {
            stateManager.commit(new Commit("Initial commit", masterBranchName));
            stateManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
