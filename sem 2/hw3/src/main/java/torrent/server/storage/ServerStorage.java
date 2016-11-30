package torrent.server.storage;

import torrent.common.storage.ServerFile;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by Mark on 04.11.2016.
 */
public class ServerStorage implements Serializable {
    //Maps id to File
    private final Map<Integer, ServerFile> files = new HashMap<>();
    //Path to server
    private static final String path = "./ServerStorage";
    private static final ServerStorage storage;

    private ServerStorage(){}

    static{
        File file = new File(path);
        ServerStorage storage1 = null;
        if(file.exists()){
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
                storage1 = (ServerStorage) ois.readObject();
                ois.close();
            } catch (IOException | ClassNotFoundException e) {
                System.err.print("Server storage is corrupted or inaccessible");
                System.exit(1);
            }
        }else {
            storage1 = new ServerStorage();
        }
        storage = storage1;
    }

    /**
     * @return list of files stored on
     * the server
     */
    public synchronized static List<ServerFile> listFiles(){
        return new ArrayList<>(storage.files.values());
    }

    /**
     * add file to storage
     * @return id of newly added file
     */
    public synchronized static int addFile(String name, Long size){
        int id = storage.files.size();
        ServerFile file = new ServerFile(id, name, size);
        storage.files.put(id, file);
        return id;
    }

    /**
     * Get file with given id.
     * @param id of file to find
     * @return File object representing file
     * with given id or null if no such file stored
     */
    public synchronized static ServerFile getFile(int id){
        return storage.files.get(id);
    }

    /**
     * add Peer to given files
     * @param address of peeer
     * @param files to be updated
     * @return whether operation succeeded
     */
    public synchronized static boolean addPeer(InetSocketAddress address, Collection<Integer> files){
        for(int id: files){
            storage.files.get(id).peers.add(address);
        }
        return true;
    }

    /**
     * Dump Server Storage to disk
     *
     * Call this method exactly once,
     * before exiting application
     */
    public static void close(){
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(storage);
            oos.close();
        } catch (IOException e) {
            System.err.println("Failed to dump Server storage to disk");
        }
    }



}
