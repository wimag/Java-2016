package torrent.client;

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
 * Created by Mark on 09.11.2016.
 */
public class ReplClient {
    private final ClientStorage storage;
    private final ClientServer server;
    private final Client client;
    public ReplClient(int port) throws IOException, ClassNotFoundException {
        storage = ClientStorageFactory.createClientStorage(port);
        server = new ClientServer(port, storage);
        client = new Client(storage);
    }

    /**
     * Start server and client repl
     */
    public void start() throws IOException {
        server.start();
        repl();
    }

    /**
     * run repl for client
     */
    private void repl(){
        System.out.println("Client started");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            try {
                String line = br.readLine();
                String[] command = line.split(" ");
                switch (command[0]){
                    case "list":
                        List<ServerFile> serverFiles = client.listFiles();
                        for(ServerFile file: serverFiles){
                            System.out.println("File Name: " + file.name);
                            System.out.println("File ID: " + file.id);
                            System.out.println("File size:" + file.size);
                            System.out.println();
                        }
                        System.out.println("A total of " + serverFiles.size() + " file found");
                        break;
                    case "get":
                        int id;
                        long size;
                        String path;
                        try {
                            id = Integer.valueOf(command[1]);
                            size = Long.valueOf(command[2]);
                            if(command.length < 4){
                                System.err.println("Please specify file path");
                                break;
                            }
                            path = command[3];
                        } catch (NumberFormatException e){
                            System.err.println("Invalud file id or size");
                            break;
                        }
                        client.downloadFile(id, path, size);
                        System.out.println("Download initiated in background");
                        break;
                    case  "upload":
                        if(command.length < 2){
                            System.err.println("Please specify file path");
                            break;
                        }
                        int uploaded = client.upload(command[1]);
                        System.out.println("Uploaded as file with id " + uploaded);
                        break;
                    case "quit":
                    case "exit":
                        stop();
                        return;
                    case "help":
                    default:
                        printHelp();
                }
            } catch (IOException ignored) {
            }

        }
    }

    public void list(){

    }

    private void printHelp() {
        System.out.println("Usage: ");
        System.out.println("list - to list files available for download");
        System.out.println("get <id> <size> <filename> - to download file");
        System.out.println("upload <filename> - to upload file");
        System.out.println("quit | exit - to quit");
    }

    /**
     * stop server and repl
     * @throws IOException
     */
    public void stop() throws IOException {
        server.stop();
        storage.close();
    }
}
