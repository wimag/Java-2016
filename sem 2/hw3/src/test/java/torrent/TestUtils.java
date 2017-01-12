package torrent;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import torrent.common.storage.ServerFile;

import java.io.*;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mark on 13.12.2016.
 */
public class TestUtils {
    private static final Random random = new Random(42);

    public static void createFile(File file, int size) throws IOException {
        RandomAccessFile f = new RandomAccessFile(file, "rw");
        f.setLength(size);
        byte[] data = new byte[size];
        random.nextBytes(data);
        f.write(data);
        f.close();
    }

    public static void clearStorages(){
        FileUtils.listFiles(new File("."), null, false).stream().filter(file -> file.getName().startsWith("ClientStorage") || file.getName().startsWith("ServerStorage")).filter(file -> !file.delete()).forEach(file -> {
            System.out.println("Couldn't delete file: " + file.getName());
        });
    }

    public static void assertEqualMetainf(ServerFile a, ServerFile b){
        assertEquals(a.size, b.size);
        assertEquals(a.name, b.name);
    }

    public static void assertEqualsFileHashes(File a, File b) throws IOException {
        FileInputStream fis = new FileInputStream(a);
        String md5a = DigestUtils.md5Hex(fis);
        fis.close();
        fis = new FileInputStream(b);
        String md5b = DigestUtils.md5Hex(fis);
        fis.close();
        assertEquals(md5a, md5b);
    }
}
