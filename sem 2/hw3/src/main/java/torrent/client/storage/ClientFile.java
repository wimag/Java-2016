package torrent.client.storage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 09.11.2016.
 */
public class ClientFile implements Serializable{
    private final String path;
    private final long size;
    private final int parts;
    private final boolean[] downloaded;
    public final long CHUNK_SIZE = 10 * 1024 * 1024; // 10 MB Chunk

    public ClientFile(String path, long size) {
        this(path, size, false);
    }

    public ClientFile(String path, long size, boolean downloaded){
        this.path = path;
        this.size = size;
        parts = (int) Math.ceil(((double) size) / CHUNK_SIZE);
        this.downloaded = new boolean[parts];
        for(int i = 0; i < parts; i++){
            this.downloaded[i] = downloaded;
        }
    }

    /**
     * @return list of available pards
     */
    public synchronized List<Integer> getAvailableParts(){
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < parts; i++) {
            if(downloaded[i]){
                res.add(i);
            }
        }
        return res;
    }


    /**
     * Check if this storage has required file part
     * @param part - id of required part
     * @return - true if this part is present
     */
    public synchronized boolean hasPart(int part){
        return downloaded[part];
    }

    /**
     * Get byte array representing
     * @param part
     * @return
     */
    public synchronized byte[] getPart(int part) throws IOException {
        byte[] res = new byte[getPartSize(part)];
        RandomAccessFile raf = new RandomAccessFile(path, "r");
        raf.seek(part * CHUNK_SIZE);
        raf.readFully(res);
        return res;
    }

    public synchronized void setPart(int part, byte[] bytes) throws IOException{
        RandomAccessFile raf = new RandomAccessFile(path, "w");
        raf.seek(part * CHUNK_SIZE);
        raf.write(bytes);
    }

    /**
     * get number of parts fir this file
     * @return
     */
    public int numberOfParts(){
        return parts;
    }


    /**
     * Get size of giver part
     * @param part
     * @return
     */
    public int getPartSize(int part){
        if(part != parts-1){
            return (int) CHUNK_SIZE;
        }
        return (int) (size - part * CHUNK_SIZE);
    }

}
