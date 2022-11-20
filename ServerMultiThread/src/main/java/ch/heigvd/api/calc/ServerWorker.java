package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());
    private Socket client = null;
    private BufferedReader  in = null;
    private BufferedWriter out = null;
    private final static String ENDOFCMD = "CRLF\r\n";

    /**
     * Instantiation of a new worker mapped to a socket
     *
     * @param clientSocket connected to worker
     */
    public ServerWorker(Socket clientSocket) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        /* TODO: prepare everything for the ServerWorker to run when the
         *   server calls the ServerWorker.run method.
         *   Don't call the ServerWorker.run method here. It has to be called from the Server.
         */
        /* Personal Note: new Thread(...).start calls the run method */
        try {
            client = clientSocket;
            in  = new BufferedReader(new InputStreamReader(client.getInputStream()  , StandardCharsets.UTF_8));
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot create client I/O socket", ex);
        }

    }

    /**
     * Run method of the thread.
     */
    @Override
    public void run() {

        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */

        try {
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

        while(true) {
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
                    out.close();
                    in.close();
                    client.close();
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