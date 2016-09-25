package hw1.command;

import hw1.exceptions.MalformedCommandException;

/**
 * Created by Mark on 25.09.2016.
 */
class HelpCommand implements Command {
    @Override
    public void execute() throws MalformedCommandException {
        System.out.println("Sort usage guide: \n" +
                "gut init - to create repo \n" +
                "gut commit -m \"message\" - to commit current files\n" +
                "gut checkout -c \"commit_id\" - to checkout specified commit \n" +
                "gut checkout -b \"branch\" - to checkout specified branch " +
                "gut branch -b \"branch name\" - to create branch \n " +
                "gut branch -d -b \"branch name\" - to delete branch \n" +
                "gut log - to show log \n" +
                "gut merge \"branch\" - to merge specified branch into current \n");
    }
}
