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
        var vowelDroppingProxyServerPort = 8000;
        var loggingServerPort = 8001;

        var loggingServer = new LoggingServer(loggingServerPort);
        var vowelDroppingProxy = new VowelDroppingProxyServer(vowelDroppingProxyServerPort, host, loggingServerPort);
        var client = new Client(host, vowelDroppingProxyServerPort);
        client.connect();

        try (var stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            log.info("Write messages to the echo server:");
            while (true) {
                var line = stdIn.readLine();
                client.send(line);
                var response = client.receive();
                log.info("Got response: {}", response);
            }
        } catch (Exception e) {
            log.error("Something went wrong.", e);
        } finally {
            vowelDroppingProxy.shutdown();
            loggingServer.shutdown();
            client.close();
        }
    }
}

