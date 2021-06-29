package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A simple server that logs input data.
 */
class LoggingServer extends AbstractServer {

    private static final Logger log = LoggerFactory.getLogger(LoggingServer.class);

    /**
     * @param port the port number to listen on
     */
    LoggingServer(int port) throws IOException {
        super(port);
    }

    @Override
    protected void handle(String line) {
        log.info("Server received: {}", line);
    }
}
