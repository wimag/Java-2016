package torrent.client.client;

import torrent.client.storage.ClientFile;
import torrent.common.ClientQueryCodes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Mark on 29.11.2016.
 *
 * downloads a file given peer list
 */
public class FileDownloader {
    private final ClientFile file;
    private final BlockingQueue<Integer> pending;
    private final List<List<InetSocketAddress>> clientsWithPart;
    private final Map<InetSocketAddress, Integer> connectoins = new HashMap<>();
    private final int fileId;
    private final int MAX_WORKERS = 5;
    private final ExecutorService pool = Executors.newFixedThreadPool(MAX_WORKERS);

    public FileDownloader(ClientFile file, int fileId, List<InetSocketAddress> peers) {
        this.file = file;
        this.fileId = fileId;
        pending = new ArrayBlockingQueue<>(file.numberOfParts());
        for (int i = 0; i < file.numberOfParts(); i++) {
            pending.add(i);
        }
        clientsWithPart = new CopyOnWriteArrayList<>();
        for (int i = 0; i < peers.size(); i++) {
            clientsWithPart.add(new CopyOnWriteArrayList<>());
        }

        Socket socket = null;
        for(InetSocketAddress peer: peers){
            try {
                connectoins.put(peer, 0);
                socket = new Socket(peer.getAddress(), peer.getPort());
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                DataInputStream is = new DataInputStream(socket.getInputStream());
                os.writeByte(ClientQueryCodes.STAT_QUERY);
                os.writeInt(fileId);
                os.flush();
                int n = is.readInt();
                for (int i = 0; i < n; i++) {
                    int part = is.readInt();
                    clientsWithPart.get(part).add(peer);
                }
                os.writeByte(ClientQueryCodes.EXIT_QUERY);
                os.flush();
            } catch (IOException e) {
                System.out.println("Unable to connect to peer " + peer);
            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    System.err.println("Failed to close socket");
                }
            }
        }
    }

    /**
     * Start download in the background
     */
    public void initDownload(){
        for (int i = 0; i < MAX_WORKERS; i++) {
            pool.submit(new DownloadWorker());
        }
        pool.shutdown();
    }

    synchronized private InetSocketAddress getPeerForPart(int part){
        int minv = Integer.MAX_VALUE;
        InetSocketAddress res = null;
        for(InetSocketAddress peer: clientsWithPart.get(part)){
            if(connectoins.get(peer) < minv){
                minv = connectoins.get(peer);
                res = peer;
            }
        }
        return res;
    }

    synchronized private void startDownload(InetSocketAddress peer){
        connectoins.put(peer, connectoins.get(peer) + 1);
    }

    synchronized private void endDownload(InetSocketAddress peer){
        connectoins.put(peer, connectoins.get(peer) - 1);
    }

    private final class DownloadWorker implements Runnable{
        private final long POLL_TIMEOUT = 2;
        @Override
        public void run() {
            Socket socket = null;
            while(!pending.isEmpty()){
                Integer part = null;
                try {
                    part = pending.poll(POLL_TIMEOUT, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    break;
                }
                if (part == null) {
                    continue;
                }
                InetSocketAddress peer = getPeerForPart(part);
                startDownload(peer);
                try {
                    socket = new Socket(peer.getAddress(), peer.getPort());
                    DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                    DataInputStream is = new DataInputStream(socket.getInputStream());
                    os.writeByte(ClientQueryCodes.GET_QUERY);
                    os.writeInt(fileId);
                    os.flush();
                    byte[] raw = new byte[file.getPartSize(part)];
                    is.readFully(raw);
                    file.setPart(part, raw);
                    os.writeByte(ClientQueryCodes.EXIT_QUERY);
                    os.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            System.err.println("Unable to close socket");
                        }
                    }
                }
                endDownload(peer);
            }
        }


    }
}
