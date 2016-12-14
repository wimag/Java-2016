package torrentGUI;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import torrent.client.Client;
import torrent.client.SimpleClient;
import torrent.client.client.ClientClient;
import torrent.client.server.ClientServer;
import torrent.client.storage.ClientStorage;
import torrent.client.storage.ClientStorageFactory;
import torrent.common.server.Server;
import torrent.server.TrackerServer;
import torrentGUI.scnene.GUI;

import java.io.IOException;

/**
 * Created by Mark on 14.12.2016.
 */
public class GuiClient implements Client {
    public final ClientStorage storage;
    public final ClientServer server;
    public final ClientClient client;

    public GuiClient(int port) throws IOException, ClassNotFoundException {
        storage = ClientStorageFactory.createClientStorage(port);
        server = new ClientServer(port, storage);
        client = new ClientClient(storage);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 600;
        config.height = 400;
        config.forceExit = false;
        //config.resizable = false;
        GUI gui = new GUI(this);
        new LwjglApplication(gui, config);
        start();
    }

    /**
     * Start server and other repl
     */
    public void start() throws IOException {
        server.start();
        //DEBUG
        try {
            other = SimpleClient.nextInstance();
            other.start();
            other.clientClient.upload("E:\\Large stuff\\tmp\\res1");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * stop server and repl
     * @throws IOException
     */
    public void stop() throws IOException {
        server.stop();
        storage.close();
        //DEBUG
        TrackerServer.stopServer();
        other.stop();
    }

    //DEBUG
    private SimpleClient other;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TrackerServer.runServer(false);
        new GuiClient(2234);
    }
}
