package hw1;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by Mark on 22.09.2016.
 */
public class GutUtils {
    private static String storagePath = ".gut";

    /**
     * Check if current directory has repo initialized
     * (.gut subfolder)
     *
     * @return true, if repo was initialized in this directory
     */
    public static boolean repoInitialized() {
        String path = System.getProperty("user.dir");
        File[] subdirs = new File(path).listFiles(File::isDirectory);
        return Arrays.asList(subdirs).contains(new File(path, storagePath));
    }

    /**
     * Get path to repository initialized in current working dir
     *
     * @return Path to repo
     */
    public static String getRepoPath() {
        return Paths.get(getCurrentPath(), storagePath).toString();
    }

    /**
     * @return path to current working folder
     */
    public static String getCurrentPath() {
        return System.getProperty("user.dir");
    }


}
