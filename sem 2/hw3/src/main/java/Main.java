import torrent.client.ReplClient;
import torrent.server.TrackerServer;

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
        if(args[0].equals("server")){
            TrackerServer.runServer();
        } else {
            try {
                int port = Integer.valueOf(args[1]);
                new ReplClient(port).start();
            } catch (NumberFormatException e){
                System.err.println(args[1] + " is not a valid porn");
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
