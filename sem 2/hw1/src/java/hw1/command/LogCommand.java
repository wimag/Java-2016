package hw1.command;

import hw1.GutUtils;
import hw1.exceptions.MalformedCommandException;
import hw1.exceptions.NoSuchRepositoryException;
import hw1.structure.Commit;
import hw1.structure.StateManager;

import java.io.IOException;

/**
 * Created by Mark on 25.09.2016.
 * <p>
 * Class represents log command - show commit history in
 * current branch
 */
class LogCommand implements Command {
    @Override
    public void execute() throws MalformedCommandException {
        try {
            if (!GutUtils.repoInitialized()) {
                throw new NoSuchRepositoryException();
            }
            StateManager stateManager = StateManager.load();
            Commit commit = stateManager.getCommitByID(stateManager.getHeadId());
            StringBuilder builder = new StringBuilder();
            while (commit.branchName.equals(stateManager.getCurrentBranch().name)) {
                builder.append("Commit id: ")
                        .append(commit.id)
                        .append("\nOn branch: ")
                        .append(commit.branchName).append("\nMessage: ").append(commit.message).append("\n\n");
                if (commit.parentId.equals(commit.id)) {
                    break;
                }
                commit = stateManager.getCommitByID(commit.parentId);

            }
            builder.append("\nBranch ").append(stateManager.getCurrentBranch().name).append(" created");
            System.out.println(builder.toString());
            stateManager.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
