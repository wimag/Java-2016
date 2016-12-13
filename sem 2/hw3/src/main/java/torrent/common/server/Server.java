package torrent.common.server;

import torrent.common.query.ExitQuery;
import torrent.common.query.Query;
import torrent.common.query.QueryFactory;
import torrent.server.storage.ConnectionManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Mark on 04.11.2016.
 */
public class Server implements Runnable{
    private volatile boolean stopped = false;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final ServerSocket listener;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    protected final QueryFactory queryFactory;

    public Server(int port, QueryFactory queryFactory) throws IOException {
        listener = new ServerSocket(port);
        this.queryFactory = queryFactory;
    }

    /**
     * start server in separate thread if it is not
     * already running
     */
    public void start() {
        if (started.compareAndSet(false, true)) {
            new Thread(this).start();
        } else {
            System.err.println("Server already running");
        }
    }

    /**
     * stop server
     *
     * @throws IOException
     */
    public void stop() throws IOException {
        stopped = true;
        listener.close();
        pool.shutdown();
    }

    /**
     * listen to connections and run them in separate threads
     */
    public void run() {
        while (!stopped) {
            Socket socket;
            try {
                socket = listener.accept();
                pool.execute(new ServerConnection(socket));
            } catch (SocketException e) {
                System.out.println("Connection closed");
                try {
                    stop();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * class represents sing connection with client
     * <p>
     * it reads query from socket, responds to it and
     * closes connection.
     */
    private class ServerConnection implements Runnable {
        private final Socket socket;

        private ServerConnection(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                DataInputStream is = new DataInputStream(socket.getInputStream());
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                Query query = null;
                while(!(query instanceof ExitQuery)){
                    query = queryFactory.createQuery(is, socket.getInetAddress());
                    query.execute(os);
                    os.flush();
                }

                os.close();

            } catch (IOException ignored) {
                System.err.println("IO Error while communication with client");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Unable to close socket");
                }
            }
        }
    }
}
