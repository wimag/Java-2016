package hw1.command;

/**
 * Created by Mark on 25.09.2016.
 */
public class CommandFactory {
    public static Command createCommand(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify command");
            return new HelpCommand();
        }
        String[] params = new String[args.length - 1];
        System.arraycopy(args, 1, params, 0, args.length - 1);
        switch (args[0].toLowerCase()) {
            case "init":
                if (params.length != 0) {
                    System.out.println("init command doesn't take arguments");
                    return new HelpCommand();
                }
                return new InitCommand();
            case "help":
                return new HelpCommand();
            case "commit":
                return new CommitCommand(params);
            case "checkout":
                return new CheckoutCommand(params);
            case "branch":
                return new BranchCommand(params);
            case "log":
                if (params.length != 0) {
                    System.out.println("log command doesn't take arguments");
                    return new HelpCommand();
                }
                return new LogCommand();
            case "merge":
                return new MergeCommand(params);
            case "add":
                return new AddCommand(params);
            case "status":
                return new StatusCommand();
            case "reset":
                return new ResetCommand(params);
            case "rm":
                return new RemoveCommand(params);
            case "clean":
                return new CleanCommand();
            default:
                System.out.println("Unknown command provided");
                return new HelpCommand();
        }
    }
}
