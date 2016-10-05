package hw1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
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

    /**
     * Check if files are the same by
     * comparing their MD5 hashes
     * @param path1 - first file
     * @param path2 - second file
     * @return - true of files are equal,
     * false otherwise
     * @throws IOException
     */
    public static boolean equalFiles(String path1, String path2) throws IOException {
        return Files.exists(Paths.get(path1)) && Files.exists(Paths.get(path2)) && getFileMD5(path1).equals(getFileMD5(path2));
    }

    /**
     * get MD5 hash of specified file
     * @param path - path to file
     * @return string representation of md5
     * @throws IOException
     */
    public static String getFileMD5(String path) throws IOException {
        FileInputStream fis = new FileInputStream(new File(path));
        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
        fis.close();
        return md5;
    }
}
