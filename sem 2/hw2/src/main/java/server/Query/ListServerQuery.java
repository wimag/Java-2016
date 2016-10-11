package server.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Created by Mark on 11.10.2016.
 * <p>
 * server implementations  of List command
 */
public class ListServerQuery implements ServerQuery {
    private final String dirname;

    public ListServerQuery(String dirname) {
        this.dirname = dirname;
    }

    public void respond(DataOutputStream stream) throws IOException {
        Path path = Paths.get(dirname);
        if (Files.exists(path) && Files.isDirectory(path)) {
            Collection<File> files = FileUtils.listFilesAndDirs(new File(dirname), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            stream.writeInt(files.size());
            for (File file : files) {
                stream.writeUTF(file.getPath());
                stream.writeBoolean(file.isDirectory());
            }
        } else {
            stream.writeInt(0);
        }
    }
}
