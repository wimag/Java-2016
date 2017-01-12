package torrent.client.client;

import org.apache.commons.io.IOUtils;
import torrent.client.storage.ClientFile;
import torrent.client.storage.ClientStorage;
import torrent.common.Config;
import torrent.common.TrackerQueryCodes;
import torrent.common.storage.ServerFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mark on 09.11.2016.
 */
public class ClientClient {
    private final ClientStorage storage;
    private Socket socket;
    public ClientClient(ClientStorage storage) {
        this.storage = storage;
    }

    /**
     * Upload file to master server.
     * @param filename - path to file to be added
     * @return id of uploaded file
     * @throws IOException
     */
    public int upload(String filename) throws IOException {
        Path path = Paths.get(filename);
        if(!Files.exists(path) || Files.isDirectory(path)){
            System.err.println("Invalid file provided");
            throw new IllegalArgumentException();
        }
        connect();
        try{
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            DataInputStream is = new DataInputStream(socket.getInputStream());
            os.writeByte(TrackerQueryCodes.UPLOAD_QUERY);
            os.writeUTF(filename);
            os.writeLong(Files.size(path));
            os.flush();
            int id = is.readInt();
            storage.addFile(filename, id, true);
            os.writeByte(TrackerQueryCodes.EXIT_QUERY);
            os.flush();
            return id;
        } catch (IOException e){
            System.out.println("Failed to upload the file");
            throw e;
        } finally {
            disconnect();
        }
    }

    /**
     * list files on master server
     * @return List of files in the network
     * @throws IOException
     */
    public List<ServerFile> listFiles() throws IOException {
        connect();
        try{
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            DataInputStream is = new DataInputStream(socket.getInputStream());
            os.writeByte(TrackerQueryCodes.LIST_QUERY);
            os.writeByte(1);
            os.flush();
            int n = is.readInt();
            List<ServerFile> res = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                res.add(new ServerFile(is.readInt(), is.readUTF(), is.readLong()));
            }
            os.writeByte(TrackerQueryCodes.EXIT_QUERY);
            os.flush();
            return res;
        }catch (IOException e){
            System.out.println("Fail to fetch file list");
            throw e;
        } finally {
            disconnect();
        }
    }

    public void downloadFile(int id, String filename, long size) throws IOException {
        List<InetSocketAddress> addrs = getPeers(id);
        ClientFile file = storage.createFile(filename, id, size);
        FileDownloader downloader = new FileDownloader(file, id, addrs);
        downloader.initDownload();
    }

    /**
     * List peers of given file
     */
    public List<InetSocketAddress> getPeers(int id) throws IOException {
        connect();
        try{
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            DataInputStream is = new DataInputStream(socket.getInputStream());
            os.writeByte(TrackerQueryCodes.SOURCES_QUERY);
            os.writeInt(id);
            os.flush();
            List<InetSocketAddress> addrs = getPeersFromStream(is);

            os.writeByte(TrackerQueryCodes.EXIT_QUERY);
            os.flush();
            return addrs;
        } catch (IOException e){
            System.err.println("Unable to get peers");
            return null;
        } finally {
            disconnect();
        }
    }

    /**
     * Perform connection to remote
     * (connects to server by default)
     * @throws IOException
     */
    private void connect() throws IOException {
        connect(Config.SERVER_ADDRES);
    }

    /**
     * Connect to given address;
     * @param addr address to connect to
     * @throws IOException
     */
    private void connect(InetSocketAddress addr) throws IOException{
        disconnect();
        socket = new Socket(addr.getAddress(), addr.getPort());
    }

    private void disconnect() {
        try{
            if (socket != null) {
                if(!socket.isClosed()){
                    socket.getOutputStream().flush();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                socket.close();
            }
        } catch (IOException e){
            System.out.println("Can not disconnect from server");
        }

    }

    private List<InetSocketAddress> getPeersFromStream(DataInputStream is) throws IOException {
        int n = is.readInt();
        List<InetSocketAddress> addrs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int addess = is.readInt();
            short port = is.readShort();
            InetAddress inetAddress = InetAddress.getByAddress(BigInteger.valueOf(addess).toByteArray());
            addrs.add(new InetSocketAddress(inetAddress, port));
        }
        return addrs;
    }
}
