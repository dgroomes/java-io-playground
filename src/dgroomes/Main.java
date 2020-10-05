package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

/**
 * See the README.md for more information.
 */
public class Main {

    private static final Logger log;

    static {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
        System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "TRUE");
        System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "HH:mm:ss");
        log = LoggerFactory.getLogger(Main.class);
    }

    public static void main(String[] args) throws Exception {
        log.info("Hello world!");

        var dataGenerator = new TestDataGenerator();
        var generatedFile = dataGenerator.generateLargeFile();
        log.debug("Generated a large file to {}", generatedFile.getAbsolutePath());
    }
}
