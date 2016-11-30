package torrent.server;

import torrent.common.Config;
import torrent.common.server.Server;
import torrent.server.query.TrackerQueryFactory;
import torrent.server.storage.ConnectionManager;
import torrent.server.storage.ServerStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Mark on 08.11.2016.
 *
 * Singleton Tracker server
 */
public class TrackerServer extends Server {
    private static TrackerServer instance;
    static {
        try {
            instance = new TrackerServer(Config.SERVER_ADDRES.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TrackerServer(int port) throws IOException {
        super(port, new TrackerQueryFactory());
        ConnectionManager connectionManager = new ConnectionManager();
        ((TrackerQueryFactory) queryFactory).setConnectionManager(connectionManager);
    }



    /**
     * run server in console
     */
    public static void runServer(){
        instance.start();
        try {
            instance.repl();
        } catch (IOException e) {
            System.err.println("Failed server repl");
        } finally {
            try {
                instance.stop();
            } catch (IOException e) {
                System.err.println("Couldn't close server");
            }
        }
    }

    /**
     * simple server reple
     * @throws IOException
     */
    private void repl() throws IOException {
        System.out.println("Server started");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            String line = br.readLine();
            String[] command = line.split(" ");
            switch (command[0]){
                case "quit":
                case "exit":
                    return;
                default:
                    System.out.println("quit|exit - to exit");
            }
        }
    }

    @Override
    public void stop() throws IOException {
        super.stop();
        ServerStorage.close();
}
}
