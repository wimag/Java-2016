import torrent.client.client.Client;
import torrent.client.server.ClientServer;
import torrent.client.storage.ClientStorage;
import torrent.client.storage.ClientStorageFactory;
import torrent.common.storage.ServerFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Mark on 13.12.2016.
 */
public class TestClient {
    final ClientStorage storage;
    final ClientServer server;
    final Client client;
    private static volatile Integer port = 1234;

    public TestClient(int port) throws IOException, ClassNotFoundException {
        storage = ClientStorageFactory.createClientStorage(port);
        server = new ClientServer(port, storage);
        client = new Client(storage);
    }

    /**
     * Start server and client repl
     */
    public void start() throws IOException {
        server.start();
    }

    /**
     * stop server and repl
     * @throws IOException
     */
    public void stop() throws IOException {
        server.stop();
        storage.close();
    }

    public synchronized static TestClient nextInstance() throws IOException, ClassNotFoundException {
        return new TestClient(port++);
    }
}
