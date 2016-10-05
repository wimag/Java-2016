package hw1.command;

import hw1.GutUtils;
import hw1.exceptions.MalformedCommandException;
import hw1.exceptions.NoSuchRepositoryException;
import hw1.structure.StateManager;

import java.io.IOException;

/**
 * Created by Mark on 05.10.2016.
 */
public class ResetCommand implements Command {
    private final String[] params;

    public ResetCommand(String[] params){
        this.params = params;
    }

    @Override
    public void execute() throws MalformedCommandException {
        if(params.length != 1){
            throw new MalformedCommandException("Reset command takes exactly one argument - filename");
        }
        if (!GutUtils.repoInitialized()) {
            throw new NoSuchRepositoryException();
        }
        try {
            StateManager stateManager = StateManager.load();
            stateManager.reset(params[0]);
            stateManager.close();
            System.out.println("File successfully reset");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
