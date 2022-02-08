package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;

/**
 * See the README.md for more information.
 */
public class FileReaderMain {

    private static final Logger log;

    static {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
        System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "TRUE");
        System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "HH:mm:ss");
        log = LoggerFactory.getLogger(FileReaderMain.class);
    }

    public static void main(String[] args) throws Exception {
        int numArgs = args.length;
        if (numArgs != 2) {
            throw new IllegalArgumentException("Expected exactly two arguments but found %d".formatted(numArgs));
        }
        var file = validateFileArg(args[0]);
        var readTechnique = validateReadTechniqueArg(args[1]);

        log.info("Reading lines from the file: {}", file.getAbsolutePath());
        long linesRead = switch (readTechnique) {
            case BUFFERED -> readLines_bufferedReader(file);
            case NIO -> readlines_nio(file);
            case STREAMS -> readlines_java8stream(file);
        };

        String linesReadPretty = NumberFormat.getInstance().format(linesRead);
        log.info("Read {} lines from the file", linesReadPretty);
    }

    /**
     * Validate the 'file' argument and return a File object representing it.
     * @param path the program argument that should be a path to file
     * @return a File object that is guaranteed to exist
     */
    private static File validateFileArg(String path) {
        if (path.isBlank()) {
            throw new IllegalArgumentException("The path argument was blank!");
        }

        File file = Path.of(path).toAbsolutePath().normalize().toFile();
        if (!file.exists()) {
            throw new IllegalStateException("The specified file does not exist! File: %s".formatted(file.getAbsolutePath()));
        }

        return file;
    }

    /**
     * Validate the 'read technique' argument and return a ReadTechnique enum value representing it.
     * @param readTechniqueArg the 'read technique' program argument that should be named after a valid read technique
     * @return a ReadTechnique enum value representing the argument
     */
    private static ReadTechnique validateReadTechniqueArg(String readTechniqueArg) {
        for (ReadTechnique readTechnique : ReadTechnique.values()) {
            if (readTechnique.name().equalsIgnoreCase(readTechniqueArg)) {
                return readTechnique;
            }
        }
        throw new IllegalArgumentException("Read technique not recognized: '%s'".formatted(readTechniqueArg));
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
}
