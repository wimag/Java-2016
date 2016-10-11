package server.Query;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Mark on 11.10.2016.
 * <p>
 * Simple interface that represents server queries
 */
public interface ServerQuery {

    /**
     * respond to query
     *
     * @param stream - where to send result
     */
    void respond(DataOutputStream stream) throws IOException;
}
