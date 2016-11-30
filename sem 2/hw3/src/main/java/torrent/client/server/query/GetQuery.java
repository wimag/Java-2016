package torrent.client.server.query;

import torrent.client.storage.ClientFile;
import torrent.client.storage.ClientStorage;
import torrent.common.query.Query;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Mark on 09.11.2016.
 */
public class GetQuery extends Query {
    private final DataInputStream is;
    private final ClientStorage storage;

    public GetQuery(DataInputStream is, ClientStorage storage) {
        super(is);
        this.is = is;
        this.storage = storage;
    }

    /**
     * Respond to Get query. write to stream a chunk of required file
     * @param os - stream, that will contain query response
     * @throws IOException
     */
    @Override
    public void execute(DataOutputStream os) throws IOException {
        int id = is.readInt();
        int part = is.readInt();
        ClientFile file = storage.getFile(id);
        if((file == null) || !file.hasPart(part)){
            return;
        }
        byte[] res = file.getPart(part);
        os.write(res);
    }
}
