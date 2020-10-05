package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * See the README.md for more information.
 */
public class Main {

    private static final Logger log;

    static {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
        log = LoggerFactory.getLogger(Main.class);
    }

    public static void main(String[] args) throws Exception {
        log.info("Hello world!");

        var dataGenerator = new TestDataGenerator();
        dataGenerator.generateLargeFile();
    }
}
