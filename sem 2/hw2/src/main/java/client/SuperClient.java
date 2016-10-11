package client;

import commons.Commands;
import exceptions.ConnectionIsDeadException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    public void connect(String host, Integer port) throws IOException {
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
     * execute read command
     *
     * @param path to file on the server
     * @return byte[] - representation of the file
     * zero length array if no such file on the server
     * @throws IOException
     */
    public byte[] getFile(String path) throws IOException {
        if (socket.isClosed()) {
            throw new ConnectionIsDeadException();
        }
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        DataInputStream is = new DataInputStream(socket.getInputStream());
        os.writeInt(Commands.GET);
        os.writeUTF(path);
        int n = is.readInt();
        byte[] response = new byte[n];
        is.read(response);
        return response;
    }

    /**
     * Disconnect from server
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        os.writeInt(Commands.EXIT);
        os.writeBytes("I'm leaving");

    }

    /**
     * class represents server file,
     * a pair of path and a flag,
     * whether it is a directory
     */
    public static class ServerFile {
        public final String path;
        public final Boolean isDirectory;

        public ServerFile(String path, Boolean isDirectory) {
            this.path = path;
            this.isDirectory = isDirectory;
        }
    }
}
