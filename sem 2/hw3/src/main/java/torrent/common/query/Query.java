package torrent.common.query;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Mark on 04.11.2016.
 */
public abstract class Query {
    public Query(DataInputStream is){}
    public abstract void execute(DataOutputStream os) throws IOException;
}
