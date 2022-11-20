package ch.heigvd.api.calc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator client implementation
 */
public class Client {

    private static final Logger LOG = Logger.getLogger(Client.class.getName());
    private final static int PORT = 12345;

    /**
     * Main function to run client
     *
     * @param args no args required
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        BufferedReader stdin = null;

        /* TODO: Implement the client here, according to your specification
         *   The client has to do the following:
         *   - connect to the server
         *   - initialize the dialog with the server according to your specification
         *   - In a loop:
         *     - read the command from the user on stdin (already created)
         *     - send the command to the server
         *     - read the response line from the server (using BufferedReader.readLine)
         */

        stdin = new BufferedReader(new InputStreamReader(System.in));
        Socket server = null;
        BufferedReader  in = null;
        BufferedWriter out = null;

        try {
            /* Create socket to server + Make connection request on IP/port */
            server = new Socket("192.168.1.122", PORT);

            in  = new BufferedReader(new InputStreamReader(server.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));

            /* Read first message from server */
            /* Must be WELCOME + available COMMANDS */
            while (true) {
                String line = in.readLine();
                System.out.println(line);
                if (line.equals("- ENDOFOPERATIONS CRLF"))
                    break;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Cannot create client I/O socket", ex);
        }

        while (true) {
            try {
                System.out.println("C: ");
                String userInput = stdin.readLine();

                out.write(userInput + "\r\n");
                out.flush();    // /!\ ABSOLUTELY NEEDED /!\

                String serverResponse = in.readLine();
                if (serverResponse == null) {
                    System.out.println("Server closed connection");
                    break;
                }

                String[] tokens = serverResponse.split(" ");
                System.out.println("S: " + serverResponse);
                if (tokens[0].equals("RESULT")) {
                    int result = Integer.parseInt(tokens[1]);
                    System.out.println("Result as int: " + result);
                }
                if (userInput.equals("QUIT"))
                    break;
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Cannot create client I/O socket", ex);
            }
        }
    }
}
