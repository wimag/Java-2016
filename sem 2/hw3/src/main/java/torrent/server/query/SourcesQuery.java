package torrent.server.query;

import torrent.common.query.Query;
import torrent.server.storage.ConnectionManager;
import torrent.common.storage.ServerFile;
import torrent.server.storage.ServerStorage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by Mark on 08.11.2016.
 */
public class SourcesQuery extends Query {
    private final DataInputStream is;
    private final ConnectionManager connectionManager;

    public SourcesQuery(DataInputStream is, ConnectionManager connectionManager) {
        super(is);
        this.is = is;
        this.connectionManager = connectionManager;
    }

    /**
     * Resopond to Sources query. write to stream a collection
     * of live peers
     * @param os - stream, that will contain query response
     * @throws IOException
     */
    @Override
    public void execute(DataOutputStream os) throws IOException {
        int id = is.readInt();
        ServerFile file = ServerStorage.getFile(id);
        List<InetSocketAddress> peers = connectionManager.filterDead(file.peers);
        os.writeInt(peers.size());
        for(InetSocketAddress address: peers){
            os.write(address.getAddress().getAddress());
            os.writeShort((short)address.getPort());
        }
    }
}
