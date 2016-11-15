package client;

import commons.Commands;
import exceptions.ConnectionIsDeadException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 11.10.2016.
 */
public class SuperClient {
    private Socket socket;

    /**
     * connect to server by given address
     *
     * @param host - address of server
     * @param port to connect to
     * @throws IOException
     */
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    /**
     * execute list command
     *
     * @param path to server directory
     * @return list of ServerFiles
     * @throws IOException
     */
    public List<ServerFile> listDir(String path) throws IOException {
        if (socket.isClosed()) {
            throw new ConnectionIsDeadException();
        }
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        DataInputStream is = new DataInputStream(socket.getInputStream());
        os.writeInt(Commands.LIST);
        os.writeUTF(path);
        os.flush();
        int n = is.readInt();
        List<ServerFile> response = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String serverPath = is.readUTF();
            Boolean isDirectory = is.readBoolean();
            response.add(new ServerFile(serverPath, isDirectory));
        }
        return response;
    }

    /**
     * create a local copy of server file
     * @param path - to server file, file at the
     *             same local location will be created
     * @return true of operation succeeded, false otherwise
     */
    public boolean getFile(String path) throws IOException {
        return getFile(path, path);
    }

    /**
     * execute read command
     *
     * @param serverPath - path to file on the server
     * @param localPath - path, where local copy will be stored
     * @return boolean - true if operaion succeeded,
     *                   false otherwise
     * @throws IOException
     */
    public boolean getFile(String serverPath, String localPath) throws IOException {
        if (socket.isClosed()) {
            throw new ConnectionIsDeadException();
        }
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        DataInputStream is = new DataInputStream(socket.getInputStream());
        os.writeInt(Commands.GET);
        os.writeUTF(serverPath);
        os.flush();

        DataOutputStream fos;
        try {
            long n = is.readLong();
            if(n == 0){
                System.out.println("No such file on server:" + serverPath);
                return false;
            }

            fos = new DataOutputStream(new FileOutputStream(new File(localPath)));

            long copied = IOUtils.copyLarge(is, fos, 0, n);
            if(copied != n){
                System.err.println("Data stream corrupted. See result in corresponding file serverPath");
            }
            fos.close();
        } catch (IOException e){
            System.err.println("Unable to create or write to file " + localPath);
            return false;
        }


        return true;
    }

    /**
     * Disconnect from server
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        os.writeInt(Commands.EXIT);
        os.writeUTF("I'm leaving");
        os.flush();
        socket.close();
    }

    /**
     * class represents server file,
     * a pair of path and a flag,
     * whether it is a directory
     */
    public static final class ServerFile {
        public final String path;
        public final Boolean isDirectory;

        ServerFile(String path, Boolean isDirectory) {
            this.path = path;
            this.isDirectory = isDirectory;
        }
    }
}
