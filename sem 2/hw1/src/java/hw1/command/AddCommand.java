package hw1.command;

import hw1.GutUtils;
import hw1.exceptions.MalformedCommandException;
import hw1.exceptions.NoSuchRepositoryException;
import hw1.structure.Commit;
import hw1.structure.StateManager;

import java.io.IOException;

/**
 * Created by Mark on 04.10.2016.
 *
 * represents Add command in vcs
 */
public class AddCommand implements Command {
    private final String[] params;

    public AddCommand(String[] params){
        this.params = params;
    }

    @Override
    public void execute() throws MalformedCommandException {
        if(params.length != 1){
            throw new MalformedCommandException("Add command takes exactly one argument");
        }
        if (!GutUtils.repoInitialized()) {
            throw new NoSuchRepositoryException();
        }
        try {
            StateManager stateManager = StateManager.load();
            stateManager.add(params[0]);
            stateManager.close();
            System.out.println("File successfully added");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
