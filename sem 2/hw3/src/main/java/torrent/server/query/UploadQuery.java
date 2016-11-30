package torrent.server.query;

import torrent.common.query.Query;
import torrent.server.storage.ServerStorage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Mark on 08.11.2016.
 */
public class UploadQuery extends Query {
    private final DataInputStream is;

    public UploadQuery(DataInputStream is) {
        super(is);
        this.is = is;
    }

    /**
     * Add file to storage. Respond with generated file id
     * @param os - output stream, where response will be written
     * @throws IOException
     */
    @Override
    public void execute(DataOutputStream os) throws IOException {
        String name = is.readUTF();
        long size = is.readLong();
        os.writeInt(ServerStorage.addFile(name, size));
    }
}
