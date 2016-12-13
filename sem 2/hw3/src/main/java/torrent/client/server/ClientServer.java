package torrent.client.server;

import torrent.client.server.query.ClientQueryFactory;
import torrent.client.storage.ClientStorage;
import torrent.common.ClientQueryCodes;
import torrent.common.Config;
import torrent.common.TrackerQueryCodes;
import torrent.common.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Created by Mark on 09.11.2016.
 */
public class ClientServer extends Server {
    private final HeartBeat heartBeat;
    private final ClientStorage storage;
    private final int port;
    public final int HEARTBEAT_INTERVAL = 5000;

    public ClientServer(int port, ClientStorage storage) throws IOException {
        super(port, new ClientQueryFactory());
        ((ClientQueryFactory)queryFactory).setClientStorage(storage);
        this.storage = storage;
        this.port = port;
        heartBeat = new HeartBeat(HEARTBEAT_INTERVAL);
    }

    @Override
    public void start() {
        super.start();
        heartBeat.start();
    }

    @Override
    public synchronized void stop() throws IOException {
        super.stop();
        heartBeat.stop();
    }

    private final class HeartBeat implements Runnable{
        private final int HEART_BEAT_INTERVAL;
        private volatile boolean stopped = false;

        public HeartBeat(){
            this(60 * 1000);
        }

        public HeartBeat(int heart_beat_interval) {
            HEART_BEAT_INTERVAL = heart_beat_interval;
        }

        public void start(){
            new Thread(this).start();
        }

        public synchronized void stop(){
            stopped = true;
        }

        @Override
        public void run() {
            while(!stopped){
                Socket socket;
                try {
                    socket = new Socket(Config.SERVER_ADDRES.getAddress(), Config.SERVER_ADDRES.getPort());
                } catch (IOException e) {
                    stop();
                    try {
                        ClientServer.this.stop();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                    System.err.println("Unable to connecto toserver");
                    break;
                }
                try {

                    DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                    DataInputStream is = new DataInputStream(socket.getInputStream());
                    os.writeByte(TrackerQueryCodes.UPDATE_QUERY);
                    os.writeShort(port);
                    List<Integer> ids = storage.getAllIds();
                    os.writeInt(ids.size());
                    for(int id: ids){
                        os.writeInt(id);
                    }
                    os.flush();
                    boolean success = is.readBoolean();
                    if(!success){
                        stop();
                        ClientServer.this.stop();
                        break;
                    }
                    os.writeByte(TrackerQueryCodes.EXIT_QUERY);
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Unable to connecto toserver");
                    break;
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(HEART_BEAT_INTERVAL);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
