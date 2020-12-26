package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * See the README.md for more information.
 */
public class FileWriterMain {

    private static final Logger log = LoggerFactory.getLogger(FileWriterMain.class);
    private static final String TEMP_LARGE_FILE = "large-temp-file.txt";
    private static final String TEMP_FILE = "temp-file.txt";
    private static final String TEMP_DIR = "tmp";
    private static final int NUMBER_OF_LINES = 100_000_000;
    private static File tempDir;

    public static void main(String[] args) throws IOException {
        int numArgs = args.length;
        if (numArgs != 1) {
            throw new IllegalArgumentException("Expected exactly one argument but found %d".formatted(numArgs));
        }

        var option = validateOptionArg(args[0]);

        tempDir = new File(TEMP_DIR);
        if (tempDir.mkdir()) {
            log.debug("Created the temp directory");
        }

        switch (option) {
            case LARGE -> generateLargeFile();
            case APPEND -> writeToFileAppend();
            case TRUNCATE -> writeToFileTruncate();
        }
    }

    /**
     * Validate the 'write option' argument and return a WriteOption enum value representing it.
     * @param arg the 'write option' string program argument
     * @return a hydrated WriteOption enum value representing the string argument
     */
    private static WriteOption validateOptionArg(String arg) {
        for (WriteOption writeOption : WriteOption.values()) {
            if (writeOption.name().equalsIgnoreCase(arg)) {
                return writeOption;
            }
        }
        throw new IllegalArgumentException("Write option not recognized for argument '%s'".formatted(arg));
    }

    /**
     * Generate a large file.
     *
     * Why is this so slow to execute? It takes a few minutes just to generate around 4GB. Because the println flushes
     * probably, right?
     */
    private static void generateLargeFile() {
        var file = new File(tempDir, TEMP_LARGE_FILE);
        log.debug("Generating a large file to {}", file.getAbsolutePath());

        try (var writer = new PrintWriter(file)) {
            for (int i = 0; i < NUMBER_OF_LINES; i++) {
                var lineNumber = i + 1;
                writer.println("%09d: this is a line of dummy data".formatted(lineNumber));
            }
        } catch (FileNotFoundException e) {
            log.error("Unexpected error when generating large file", e);
            throw new IllegalStateException(e);
        }

        log.debug("Generated a large file to {}", file.getAbsolutePath());
    }

    /**
     * Write a hardcoded series of messages to a file, one by one. Then print out its contents.
     * @param openOption the given OpenOption will be used for the OutputStream.
     */
    private static void writeToFile(OpenOption openOption) throws IOException {
        var file = new File(tempDir, TEMP_FILE);
        var filePath = file.toPath();

        // Clean up the file if it already exists.
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        // Write the content
        var messages = List.of("Hello,\n", "world", "!\n");
        for (String message : messages) {
            Files.writeString(filePath, message, openOption);
        }

        // Read the content
        String foundContent = Files.readString(filePath);
        log.info("All messages were written to the file. The files contents:\n{}", foundContent);
    }

    /**
     * Write to a file and use the 'APPEND' {@link OpenOption}.
     */
    private static void writeToFileAppend() throws IOException {
        log.info("Will write to a file using the 'APPEND' OpenOption. The content from all write operations will be in the file");
        writeToFile(StandardOpenOption.APPEND);
    }

    /**
     * Write to a file and use the 'TRUNCATE_EXISTING' {@link OpenOption}.
     *
     * Note: if we were to omit any options to the the `Files.writeString` method, then it would actually default to
     * using 'TRUNCATE_EXISTING'. So, we are being unnecessarily explicit. See https://github.com/openjdk/jdk/blob/97c99b5d7d4fc057a7ebc378d1e7dd915eaf0bb3/src/java.base/share/classes/java/nio/file/spi/FileSystemProvider.java#L427
     */
    private static void writeToFileTruncate() throws IOException {
        log.info("Will write to a file using the 'TRUNCATE_EXISTING' OpenOption. Only the content of the last write operation will be in the file");
        writeToFile(StandardOpenOption.TRUNCATE_EXISTING);
    }
}
