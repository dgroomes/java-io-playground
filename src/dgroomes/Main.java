package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import java.io.*;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.stream.Stream;

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
//        generateTestFile();
        var file = new File("tmp/large-temp-file.txt");
        log.info("Reading lines from  the large temp file");
//        long linesRead = readLines_bufferedReader(file);
//        long linesRead = readlines_nio(file);
        long linesRead = readlines_java8stream(file);
        log.info("Read {} lines from the large temp file", NumberFormat.getInstance().format(linesRead));
    }

    /**
     * Read from file using Java 8 Streams
     */
    private static long readlines_java8stream(File file) throws IOException {
        try (var stream = Files.lines(file.toPath())) {
            return stream.count();
        }
    }

    /**
     * Read from file using java.nio (this is like exactly the same as readLines_bufferedReader)
     * <p>
     * What does it mean to have unbuffered stream?
     */
    private static long readlines_nio(File file) throws IOException {
        try (var unbufferedInputStream = Files.newInputStream(file.toPath());
             var reader = new InputStreamReader(unbufferedInputStream);
             var bufferedReader = new BufferedReader(reader)) {

            int linesRead = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                linesRead++;
            }
            return linesRead;
        }
    }

    /**
     * Read from file using {@link BufferedReader} and {@link FileReader}
     */
    private static long readLines_bufferedReader(File file) throws IOException {
        try (var reader = new FileReader(file);
             var bufferedReader = new BufferedReader(reader)) {

            int linesRead = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                linesRead++;
            }
            return linesRead;
        }
    }

    private static void generateTestFile() {
        var dataGenerator = new TestDataGenerator();
        var generatedFile = dataGenerator.generateLargeFile();
        log.debug("Generated a large file to {}", generatedFile.getAbsolutePath());
    }
}
