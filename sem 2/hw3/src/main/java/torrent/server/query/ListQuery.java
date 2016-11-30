package torrent.server.query;

import torrent.common.query.Query;
import torrent.common.storage.ServerFile;
import torrent.server.storage.ServerStorage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Mark on 04.11.2016.
 */
public class ListQuery extends Query {

    public ListQuery(DataInputStream is) {
        super(is);
    }

    /**
     * List files in the directory to output stream
     * @param os - output stream, where response will be written
     * @throws IOException
     */
    public void execute(DataOutputStream os) throws IOException {
        List<ServerFile> files = ServerStorage.listFiles();
        os.writeInt(files.size());
        for(ServerFile file: files){
            os.writeInt(file.id);
            os.writeUTF(file.name);
            os.writeLong(file.size);
        }
    }
}
