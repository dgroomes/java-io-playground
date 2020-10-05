package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import java.io.*;

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
        var file = new File("tmp/large-temp-file.txt");
        log.info("Reading lines from  the large temp file");
        int linesRead = readLines_bufferedReader(file);
        log.info("Read {} lines from the large temp file", linesRead);
    }

    /**
     * Read from file using {@link BufferedReader} and {@link FileReader}
     * @param file
     * @return
     * @throws IOException
     */
    private static int readLines_bufferedReader(File file) throws IOException {
        var reader = new BufferedReader(new FileReader(file));
        int linesRead = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            linesRead++;
        }
        return linesRead;
    }

    private static void generateTestFile() {
        var dataGenerator = new TestDataGenerator();
        var generatedFile = dataGenerator.generateLargeFile();
        log.debug("Generated a large file to {}", generatedFile.getAbsolutePath());
    }
}
