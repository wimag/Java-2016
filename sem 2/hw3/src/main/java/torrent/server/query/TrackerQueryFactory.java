package torrent.server.query;

import torrent.common.TrackerQueryCodes;
import torrent.common.query.ExitQuery;
import torrent.common.query.Query;
import torrent.common.query.QueryFactory;
import torrent.common.query.UnknownQueryException;
import torrent.server.storage.ConnectionManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Mark on 04.11.2016.
 */
public class TrackerQueryFactory extends QueryFactory {
    private ConnectionManager connectionManager = null;

    /**
     * Set connection manager for query processing
     * @param connectionManager - new connection manager (null by default)
     */
    public void setConnectionManager(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    /**
     * @param is - input stream containing query details
     * @param address - IP address of incomming connection
     * @return generated query
     * @throws IOException
     */
    @Override
    public Query createQuery(DataInputStream is, InetAddress address) throws IOException {
        Byte type = is.readByte();
        switch (type){
            case TrackerQueryCodes.EXIT_QUERY:
                return new ExitQuery(is);
            case TrackerQueryCodes.LIST_QUERY:
                return new ListQuery(is);
            case TrackerQueryCodes.UPLOAD_QUERY:
                return new UploadQuery(is);
            case TrackerQueryCodes.SOURCES_QUERY:
                return new SourcesQuery(is, connectionManager);
            case TrackerQueryCodes.UPDATE_QUERY:
                return new UpdateQuery(is, connectionManager, address);
            default:
                throw new UnknownQueryException();
        }
    }
}
