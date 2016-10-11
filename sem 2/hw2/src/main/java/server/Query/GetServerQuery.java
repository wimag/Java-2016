package server.Query;

import java.io.DataOutputStream;
import java.io.IOException;
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
            byte[] bytes = Files.readAllBytes(path);
            stream.writeInt(bytes.length);
            stream.write(bytes);
        } else {
            stream.writeInt(0);
        }
    }
}
