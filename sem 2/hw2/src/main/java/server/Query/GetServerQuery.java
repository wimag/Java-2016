package server.Query;

import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Mark on 11.10.2016.
 * <p>
 * server representation of Get command
 */
public class GetServerQuery implements ServerQuery {
    private final String filename;

    public GetServerQuery(String filename) {
        this.filename = filename;
    }

    public void respond(DataOutputStream stream) throws IOException {
        Path path = Paths.get(filename);
        if (Files.exists(path) && !Files.isDirectory(path)) {
            long size = Files.size(path);
            stream.writeLong(Files.size(path));

            FileInputStream fis = new FileInputStream(filename);
            long copied = IOUtils.copyLarge(fis, stream, 0, size);

            if (copied != size){
                System.err.println("Failed to copy file correctly");
            }
        } else {
            stream.writeLong(0);
        }
    }
}
