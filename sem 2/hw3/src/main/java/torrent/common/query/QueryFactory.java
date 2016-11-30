package torrent.common.query;

import torrent.server.storage.ConnectionManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Mark on 08.11.2016.
 */
public abstract class QueryFactory {

    /**
     * Generate query based on input stream containing query
     * @param is - input stream containing query details
     * @param address - IP address ofincomming connection
     * @return generated query
     * @throws IOException
     */
    public abstract Query createQuery(DataInputStream is, InetAddress address) throws IOException;
}
