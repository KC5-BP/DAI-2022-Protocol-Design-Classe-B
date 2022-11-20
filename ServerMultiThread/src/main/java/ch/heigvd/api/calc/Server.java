package ch.heigvd.api.calc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator server implementation - multi-thread
 */
public class Server {

    private final static Logger LOG = Logger.getLogger(Server.class.getName());
    private final static int PORT = 12345;

    /**
     * Main function to start the server
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        (new Server()).start();
    }

    /**
     * Start the server on a listening socket.
     */
    private void start() {

        /* TODO: implement the receptionist server here.
         *  The receptionist just creates a server socket and accepts new client connections.
         *  For a new client connection, the actual work is done in a new thread
         *  by a new ServerWorker.
         */
        ServerSocket server;
        Socket client = null;

        try {
            /* Create receptionnist Socket + Bind only port */
            /* By default, connect on localhost */
            server = new ServerSocket(PORT);

            /* Create receptionnist Socket +
             * Bind port, number of connections and IP */
            /*server = new ServerSocket(  PORT, 10,
                                        InetAddress.getByName("192.168.1.122"));*/
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot create server socket", ex);
            return;
        }
        System.out.println("Server started @ " + server.getInetAddress() + ":" + PORT);

        while(true) {
            try {
                /* Accept new client connection + Receive new client' socket */
                client = server.accept();
                System.out.println("New client accepted!");
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Cannot accept client connection", ex);
                return;
            }

            /* Create new thread for new client */
            new Thread(new ServerWorker(client)).start();
        }
    }
}
