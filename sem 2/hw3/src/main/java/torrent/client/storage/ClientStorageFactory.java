package torrent.client.storage;

import java.io.*;

/**
 * Created by Mark on 09.11.2016.
 */
public class ClientStorageFactory {
    private ClientStorageFactory(){}

    /**
     * Create a client Storage for a torrent-client server part
     * running on given port
     * @param port of client server
     * @return loaded storage if it already existed, or new instance
     * otherwise
     * @throws IOException | ClassNotFoundException - if storage file is
     * corrupted or unaccessable
     */
    public static ClientStorage createClientStorage(int port) throws IOException, ClassNotFoundException {
        String path = "./ClientStorage" + port;
        File file = new File(path);
        ClientStorage cs;
        if(file.exists()){
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            cs = (ClientStorage) ois.readObject();
            ois.close();
        } else {
            cs = new ClientStorage(path);
        }
        return cs;
    }
}
