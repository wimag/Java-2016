package server;

import server.Query.ExitServerQuery;
import server.Query.QueryFactory;
import server.Query.ServerQuery;

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
 * Created by Mark on 11.10.2016.
 * <p>
 * Simple server that starts serversocket listening
 * to given port and creates (server works in separate
 * thread
 */
public class SuperServer implements Runnable {
    private volatile boolean stopped = false;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final ServerSocket listener;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public SuperServer(int port) throws IOException {
        listener = new ServerSocket(port);
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
    private static class ServerConnection implements Runnable {
        private final Socket socket;

        private ServerConnection(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream is = new DataInputStream(socket.getInputStream());
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                ServerQuery query = null;
                while (!(query instanceof ExitServerQuery)) {
                    Integer type = is.readInt();
                    String path = is.readUTF();
                    query = QueryFactory.create(type, path);
                    query.respond(os);
                    os.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error while communication with client");
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Unable to close socket");
                }
            }
        }
    }
}
