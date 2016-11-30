package torrent.client.server.query;

import torrent.client.storage.ClientStorage;
import torrent.common.ClientQueryCodes;
import torrent.common.query.ExitQuery;
import torrent.common.query.Query;
import torrent.common.query.QueryFactory;
import torrent.common.query.UnknownQueryException;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Mark on 08.11.2016.
 */
public class ClientQueryFactory extends QueryFactory {
    private ClientStorage clientStorage = null;

    /**
     * set client storage to given value
     * @param clientStorage new client storage
     */
    public void setClientStorage(ClientStorage clientStorage) {
        this.clientStorage = clientStorage;
    }

    @Override
    public Query createQuery(DataInputStream is, InetAddress address) throws IOException {
        Byte type = is.readByte();
        switch (type){
            case ClientQueryCodes.EXIT_QUERY:
                return new ExitQuery(is);
            case ClientQueryCodes.STAT_QUERY:
                return new StatQuery(is, clientStorage);
            case ClientQueryCodes.GET_QUERY:
                return new GetQuery(is, clientStorage);
            default:
                throw new UnknownQueryException();
        }
    }
}
