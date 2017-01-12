package torrent.client;

import torrent.client.client.ClientClient;
import torrent.client.server.ClientServer;
import torrent.client.storage.ClientStorage;
import torrent.client.storage.ClientStorageFactory;

import java.io.IOException;

/**
 * Created by Mark on 13.12.2016.
 */
public class SimpleClient implements Client {
    public final ClientStorage storage;
    public final ClientServer server;
    public final ClientClient clientClient;
    private static volatile Integer port = 1234;

    public SimpleClient(int port) throws IOException, ClassNotFoundException {
        storage = ClientStorageFactory.createClientStorage(port);
        server = new ClientServer(port, storage);
        clientClient = new ClientClient(storage);
    }

    /**
     * Start server and client
     */
    public void start() throws IOException {
        server.start();
    }

    /**
     * stop server
     * @throws IOException
     */
    public void stop() throws IOException {
        server.stop();
        storage.close();
    }

    public synchronized static SimpleClient nextInstance() throws IOException, ClassNotFoundException {
        return new SimpleClient(port++);
    }
}
