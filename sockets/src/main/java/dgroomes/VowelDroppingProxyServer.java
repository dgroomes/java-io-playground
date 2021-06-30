package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A server that listens for input strings, drops all vowels, and forwards them (rather, "proxies") to another server.
 */
class VowelDroppingProxyServer extends AbstractServer {

    private static final Logger log = LoggerFactory.getLogger(VowelDroppingProxyServer.class);
    private final Client client;

    /**
     * @param port           the port number to listen on
     * @param downstreamHost the host of the downstream server that this server outputs data to
     * @param downstreamPort the port of the downstream server that this server outputs data to
     */
    VowelDroppingProxyServer(int port, String downstreamHost, int downstreamPort) throws IOException {
        super(port);
        client = new Client(downstreamHost, downstreamPort);
        client.connect();
    }

    @Override
    protected String handle(String line) {
        log.info("Server received: {}", line);
        var noVowels = line.replaceAll("[aeiouyAEIOUY]", "");
        client.send(noVowels);
        return client.receive();
    }

    @Override
    public void shutdown() throws Exception {
        super.shutdown();
        client.close();
    }
}
