package torrent.server.query;

import torrent.common.query.Query;
import torrent.server.storage.ConnectionManager;
import torrent.server.storage.ServerStorage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 08.11.2016.
 */
public class UpdateQuery extends Query {
    private final DataInputStream is;
    private final ConnectionManager connectionManager;
    private final InetAddress address;

    public UpdateQuery(DataInputStream is, ConnectionManager connectionManager, InetAddress address) {
        super(is);
        this.is = is;
        this.connectionManager = connectionManager;
        this.address = address;
    }

    @Override
    public void execute(DataOutputStream os) throws IOException {
        int port = is.readShort();
        InetSocketAddress peer = new InetSocketAddress(address, port);
        boolean status = connectionManager.registerPeer(peer);
        int count = is.readInt();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(is.readInt());
        }
        status &= ServerStorage.addPeer(peer, ids);
        os.writeBoolean(status);
    }
}
