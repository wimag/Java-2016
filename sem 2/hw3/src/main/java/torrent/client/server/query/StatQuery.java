package torrent.client.server.query;

import torrent.client.storage.ClientFile;
import torrent.client.storage.ClientStorage;
import torrent.common.query.Query;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Mark on 09.11.2016.
 */
public class StatQuery extends Query {
    private final DataInputStream is;
    private ClientStorage storage;
    public StatQuery(DataInputStream is, ClientStorage storage) {
        super(is);
        this.is = is;
        this.storage = storage;
    }

    /**
     * Resopond to Stat query. write to stream a collection
     * of available parts
     * @param os - stream, that will contain query response
     * @throws IOException
     */
    @Override
    public void execute(DataOutputStream os) throws IOException {
        int id = is.readInt();
        ClientFile file = storage.getFile(id);
        if(file == null){
            return;
        }
        List<Integer> parts = file.getAvailableParts();
        os.writeInt(parts.size());
        for(int part: parts){
            os.writeInt(part);
        }
    }
}
