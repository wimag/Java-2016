package hw1;

import hw1.command.CommandFactory;
import hw1.exceptions.MalformedCommandException;

import java.io.IOException;

/**
 * Created by Mark on 22.09.2016.
 */
public class CommandExecutor {
    public static void main(String[] args) throws IOException {
        try {
            CommandFactory.createCommand(args).execute();
        } catch (MalformedCommandException e) {
            System.err.println(e.getMessage());
        }

    }
}
