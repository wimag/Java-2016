import torrent.client.ReplClient;
import torrent.server.TrackerServer;
import torrentGUI.GuiClient;

import java.io.IOException;

/**
 * Created by Mark on 31.10.2016.
 */
public class Main {
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Please specify run arguments:");
            System.out.println("server - to run server");
            System.out.println("client <port> - to run client on given port");
            return;
        }
        switch (args[0]){
            case "server":
                TrackerServer.runServer();
                break;
            case "client":
                try {
                    int port = Integer.valueOf(args[1]);
                    new ReplClient(port).start();
                } catch (NumberFormatException e){
                    System.err.println(args[1] + " is not a valid port");
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
                break;
            case "gui":
                try {
                    int port = Integer.valueOf(args[1]);
                    new GuiClient(port).start();
                } catch (NumberFormatException e){
                    System.err.println(args[1] + " is not a valid port");
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
        }

    }
}
