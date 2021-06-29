package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * See the README.md for more information.
 */
public class SocketsMain {

    private static final Logger log = LoggerFactory.getLogger(SocketsMain.class);

    public static void main(String[] args) throws Exception {
        var host = "127.0.0.1";
        var port = 8000;

        var server = new LoggingServer(port);
        var client = new Client(host, port);
        client.connect();

        try (var stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            log.info("Write messages to the echo server:");
            while (true) {
                var line = stdIn.readLine();
                client.send(line);
            }
        } catch (Exception e) {
            log.error("Something went wrong.", e);
        } finally {
            server.shutdown();
            client.close();
        }
    }
}

