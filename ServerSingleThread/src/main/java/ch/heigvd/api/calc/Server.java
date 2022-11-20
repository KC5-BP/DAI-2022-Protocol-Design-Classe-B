package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator server implementation - single threaded
 */
public class Server {

    private final static Logger LOG = Logger.getLogger(Server.class.getName());
    private final static int PORT = 12345;
    private final static String ENDOFCMD = "CRLF\r\n";

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
         *  For a new client connection, the actual work is done by the handleClient method below.
         */
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not listen on port " + PORT, e);
            System.exit(-1);
        }

        while (true) {
            try {
                Socket client = server.accept();
                handleClient(client);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Accept failed on port " + PORT, e);
                System.exit(-1);
            }
        }

    }

    /**
     * Handle a single client connection: receive commands and send back the result.
     *
     * @param clientSocket with the connection with the individual client.
     */
    private void handleClient(Socket clientSocket) {

        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */
        BufferedReader  in = null;
        BufferedWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));

            out.write("WELCOME " + ENDOFCMD);
            out.write("- AVAILABLE OPERATIONS " + ENDOFCMD);
            out.write("- ADD 2 " + ENDOFCMD);
            out.write("- MULT 2 " + ENDOFCMD);
            out.write("- DIV 2 " + ENDOFCMD);
            out.write("- ENDOFOPERATIONS " + ENDOFCMD);
            out.flush();    // /!\ ABSOLUTELY NEEDED /!\
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            String res = "";
            try {
                String line = in.readLine();
                if(line == null) {
                    break;
                }
                String[] tokens = line.split(" ");
                if(tokens.length == 0) {
                    continue;
                }
                String cmd = tokens[0];
                if(cmd.equals("ADD")) {
                    if(tokens.length != 3) {
                        out.write("ERROR " + ENDOFCMD);
                        out.flush();
                        continue;
                    }
                    int a = Integer.parseInt(tokens[1]);
                    int b = Integer.parseInt(tokens[2]);
                    res = "RESULT " + (a + b) + " " + ENDOFCMD;
                    System.out.print(res);
                    out.write(res);
                    out.flush();
                } else if(cmd.equals("MULT")) {
                    if(tokens.length != 3) {
                        out.write("ERROR " + ENDOFCMD);
                        out.flush();
                        continue;
                    }
                    int a = Integer.parseInt(tokens[1]);
                    int b = Integer.parseInt(tokens[2]);
                    res = "RESULT " + (a * b) + " " + ENDOFCMD;
                    System.out.print(res);
                    out.write(res);
                    out.flush();
                } else if(cmd.equals("DIV")) {
                    if(tokens.length != 3) {
                        out.write("ERROR " + ENDOFCMD);
                        out.flush();
                        continue;
                    }
                    int a = Integer.parseInt(tokens[1]);
                    int b = Integer.parseInt(tokens[2]);
                    res = "RESULT " + (a / b) + " " + ENDOFCMD;
                    System.out.print(res);
                    out.write(res);
                    out.flush();
                } else if(cmd.equals("QUIT")) {
                    System.out.println("Closing resources and disconnecting client");
                    System.out.println("END OF THREAD");
                    out.close();
                    in.close();
                    clientSocket.close();
                    break;
                } else {
                    out.write("ERROR 400 SYNTAX ERROR " + ENDOFCMD);
                    out.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}