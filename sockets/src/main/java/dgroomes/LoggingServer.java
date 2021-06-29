package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A server that just listens for string input on a socket and logs the values.
 */
class LoggingServer {

    private static final Logger log = LoggerFactory.getLogger(LoggingServer.class);

    private final AtomicReference<BufferedReader> reader = new AtomicReference<>(null);
    private final Thread thread;
    private final ServerSocket serverSocket;
    private final int port;

    /**
     * @param port the port number to listen on
     */
    LoggingServer(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(this.port);

        thread = new Thread(this::connect);
        thread.start();
    }

    /**
     * Connect to the input socket
     */
    private void connect() {

        // Listen for a connection to be made
        Socket socket;
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            var msg = String.format("Failed to find a connection on port %s", port);
            throw new IllegalStateException(msg, e);
        }

        // Set up the reader object
        InputStream inputStream;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get the input stream on the socket", e);
        }
        var reader = new BufferedReader(new InputStreamReader(inputStream));
        this.reader.set(reader);

        continuouslyListen(reader);
    }

    /**
     * Continuously listen for input on the socket.
     */
    private void continuouslyListen(BufferedReader reader) {
        log.info("Listening for messages...");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                log.info("Server received: {}", line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error while reading the socket input stream.", e);
        }
    }

    /**
     * Shutdown the server
     */
    public void shutdown() throws Exception {
        reader.get().close();
        thread.interrupt(); // I can never remember how to gracefully shut things down... this is not it.
    }
}
