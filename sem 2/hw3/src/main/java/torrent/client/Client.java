package torrent.client;

import java.io.IOException;

/**
 * Created by Mark on 14.12.2016.
 */
public interface Client {
    /**
     * Start this client
     */
    void start() throws IOException;

    /**
     * stop client application
     */
    void stop() throws IOException;
}
