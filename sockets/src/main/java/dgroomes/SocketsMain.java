package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * See the README.md for more information.
 */
public class SocketsMain {

    private static final Logger log = LoggerFactory.getLogger(SocketsMain.class);

    public static void main(String[] args) throws Exception {
        var host = "127.0.0.1";
        var port = 8000;

        var server = new LoggingServer(port);
        var clientSocket = new Socket(host, port);

        try (var stdIn = new BufferedReader(new InputStreamReader(System.in));
             var socketWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)) {

            log.info("Write messages to the echo server:");

            while (true) {
                var line = stdIn.readLine();
                socketWriter.println(line);
            }
        } catch (Exception e) {
            log.error("Something went wrong.", e);
        } finally {
            server.shutdown();
        }
    }
}

