package dgroomes;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A client to the server.
 * <p>
 * This should encapsulate implementation details like connection creation.
 */
public class Client {

    private final String host;
    private final int port;
    private final AtomicReference<PrintWriter> socketWriter = new AtomicReference<>();
    private final AtomicReference<Socket> clientSocket = new AtomicReference<>();

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Create a connection to the server via a socket.
     */
    public void connect() {
        Socket clientSocket;
        try {
            clientSocket = new Socket(host, port);
            this.clientSocket.set(clientSocket);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Failed to create a socket at %s:%s", host, port));
        }

        try {
            socketWriter.set(new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true));
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Failed to get the output stream from the socket at %s:%s", host, port));
        }
    }

    /**
     * Send a message to the server via the socket.
     *
     * @param message the message to send
     */
    public void send(String message) {
        var writer = socketWriter.get();
        if (writer == null) {
            throw new IllegalStateException("No connection has been established yet with the server. Aborting.");
        }
        writer.println(message);
    }

    /**
     * My weak attempt at cleanly shutting down components of the program. Call this before program exit.
     */
    public void close() {
        var writer = socketWriter.get();
        if (writer != null) {
            writer.close();
        }
        var socket = clientSocket.get();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                throw new IllegalStateException("Failed to close the socket", e);
            }
        }
    }
}
