package torrent.client.storage;

import torrent.client.client.Client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mark on 09.11.2016.
 */
public class ClientStorage implements Serializable {

    private final Map<Integer, ClientFile> files = new HashMap<>();
    private final String path;

    public ClientStorage(String path){
        this.path = path;
    }

    /**
     * get file with given id
     * @param id of requested file
     * @return requested File
     */
    public synchronized ClientFile getFile(int id){
        return files.get(id);
    }

    /**
     * get All file ids
     * @return collection containing all file ids
     */
    public synchronized List<Integer> getAllIds(){
        return new ArrayList<>(files.keySet());
    }

    /**
     * Adds file to this storage. File is not downloaded by default
     * @param filename name of file to be added
     * @param id to be recorded
     */
    public synchronized ClientFile addFile(String filename, int id){
        return addFile(filename, id, false);
    }

    /**
     * Adds file to this storage.
     * Does nothing, if file doesn't exist
     * @param filename of file to be added
     * @param id id of this file
     * @param downloaded shows if file is already downloaded
     */
    public synchronized ClientFile addFile(String filename, int id, boolean downloaded){
        Path path = Paths.get(filename);
        long size;
        try {
            size = Files.size(path);
        } catch (IOException e) {
            return null;
        }
        ClientFile file = new ClientFile(filename, size, downloaded);
        files.put(id, file);
        return file;
    }

    /**
     * create local file and add it to index
     * @param filename of file to be added
     * @param id id of this file
     * @param size of file to be created
     * @return
     */
    public synchronized ClientFile createFile(String filename, int id, long size) throws IOException {
        Path path = Paths.get(filename);
        if(Files.exists(path)){
            System.err.println("File already exists");
            throw new IOException("File already exists");
        }
        RandomAccessFile raf = new RandomAccessFile(filename, "rw");
        raf.setLength(size);
        raf.close();
        return addFile(filename, id, false);
    }

    /**
     * dump storage to file
     */
    public void close(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            System.err.println("Failed to dump Client storage to disk:" + path);
        }
    }

}
