import client.SuperClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;
import server.SuperServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Mark on 11.10.2016.
 */
public class SuperTest {

    /**
     * get random element from collection
     *
     * @param coll - collection to search
     * @param <T>  - collection element type;
     * @return Random element
     */
    private static <T> T randomElement(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for (T t : coll) if (--num < 0) return t;
        throw new AssertionError();
    }

    /**
     * Tets List command in root directory
     *
     * @throws IOException
     */
    @Test
    public void testList() throws IOException {
        final int TEST_LIST_PORT = 1234;
        final String TEST_LIST_REAL_PATH = ".";
        final String TEST_LIST_FAKE_PATH = "NONEXISTENT_DIR";
        SuperServer server = new SuperServer(TEST_LIST_PORT);
        server.start();
        SuperClient client = new SuperClient();
        client.connect("localhost", TEST_LIST_PORT);
        List<SuperClient.ServerFile> toneOfFiles = client.listDir(TEST_LIST_REAL_PATH);
        Collection<File> realFiles = FileUtils.listFilesAndDirs(new File(TEST_LIST_REAL_PATH), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (SuperClient.ServerFile file : toneOfFiles) {
            File realFile = new File(file.path);
            assertEquals(realFile.isDirectory(), file.isDirectory);
            assertTrue(realFiles.contains(realFile));
        }
        assertEquals(realFiles.size(), toneOfFiles.size());

        List<SuperClient.ServerFile> emptyFiles = client.listDir(TEST_LIST_FAKE_PATH);
        assertEquals(0, emptyFiles.size());
        client.disconnect();
        server.stop();
    }

    /**
     * Test get command:
     * repeatedly pick a random file and try to get it
     * from server
     *
     * @throws IOException
     */
    @Test
    public void testGet() throws IOException {
        final int TEST_GET_PORT = 1235;
        final int TEST_NUM = 20;
        final String ALL_PATH = ".";
        final String NONEXISTENT_PATH = "NONEXISTENT.txt";

        Collection<File> realFiles = FileUtils.listFilesAndDirs(new File(ALL_PATH), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        SuperServer server = new SuperServer(TEST_GET_PORT);
        server.start();
        SuperClient client = new SuperClient();
        client.connect("localhost", TEST_GET_PORT);

        for(File file: realFiles){
            if(file.getPath().endsWith("cpy")){
                try{
                    Files.delete(Paths.get(file.getPath()));
                }catch (IOException e){
                    System.err.println("Unable to delete created file");
                }
            }
        }

        for (int i = 0; i < TEST_NUM; i++) {
            File testFile = randomElement(realFiles);
            if (testFile.isDirectory()){
                continue;
            }
            String path = testFile.getPath();
            String newPath = testFile.getPath() + "cpy";
            assertTrue(client.getFile(path, newPath));
            try {
                assertArrayEquals(FileUtils.readFileToByteArray(testFile), FileUtils.readFileToByteArray(testFile));
            } finally {
                try{
                    Files.delete(Paths.get(newPath));
                }catch (IOException e){
                    System.err.println("Unable to delete created file");
                }
            }
        }

        assertFalse(client.getFile(NONEXISTENT_PATH));
        client.disconnect();
        server.stop();
    }

}