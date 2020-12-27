package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Formatter;
import java.util.List;

/**
 * See the README.md for more information.
 */
public class FileWriterMain {

    private static final Logger log = LoggerFactory.getLogger(FileWriterMain.class);
    private static final String TEMP_LARGE_FILE = "large-file.txt";
    private static final String TEMP_FILE = "file.txt";
    private static final String TEMP_DIR = "tmp";
    private static final int NUMBER_OF_LINES = 100_000_000;
    private static final int KiB = 1024;
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

        // We want to use the switch expression to force exhaustiveness. We don't actually care about the return value
        // which is why it is named "_ignored". If we omitted the assignment, we would just have a switch *statement*
        // which does not force exhaustiveness! This is a bit awkward but that's just how it is! See http://openjdk.java.net/jeps/361
        var _ignored = switch (option) {
            case LARGE -> generateLargeFile();
            case LARGE_FAST -> generateLargeFileFast();
            case APPEND -> writeToFileAppend();
            case TRUNCATE -> writeToFileTruncate();
        };
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
     * Create a file "fresh" (i.e. delete it if it already existed)
     * @param file the file to create
     */
    private static void createFileFresh(File file) throws IOException {
        if (file.exists()) {
            var deleted = file.delete();
            if (!deleted) {
                throw new IllegalStateException("Failed to delete the file %s".formatted(file));
            }
        }
        var created = file.createNewFile();
        if (!created) {
            throw new IllegalStateException("Failed to create the file %s".formatted(file));
        }
    }

    /**
     * Generate a large file.
     *
     * Why is this so slow to execute? It takes a few minutes just to generate around 4GB. Because the println flushes
     * probably, right?
     */
    private static File generateLargeFile() throws IOException {
        var file = new File(tempDir, TEMP_LARGE_FILE);
        createFileFresh(file);
        var filePath = file.getAbsolutePath();
        boolean autoFlush = true;
        log.info("Generating a large file to {} with autoFlush {}", filePath, autoFlush);

        try (var writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))), autoFlush)) {
            for (int i = 0; i < NUMBER_OF_LINES; i++) {
                var lineNumber = i + 1;
                writer.println("%09d: this is a line of dummy data".formatted(lineNumber));
            }
        } catch (FileNotFoundException e) {
            log.error("Unexpected error when generating large file", e);
            throw new IllegalStateException(e);
        }

        log.info("Generated a large file to {}", filePath);
        return file;
    }


    /**
     * Generate a large file quickly.
     */
    private static File generateLargeFileFast() throws IOException {
        var file = new File(tempDir, TEMP_LARGE_FILE);
        createFileFresh(file);
        var filePath = file.getAbsolutePath();
        boolean autoFlush = false;
        int bufferSize = KiB * 32;
        log.info("Generating a large file to {} with autoFlush {} and bufferSize {}", filePath, autoFlush, bufferSize);

        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(file));
        BufferedWriter bufferedWriter = new BufferedWriter(streamWriter, bufferSize);
        var formatter = new Formatter();
        try (var writer = new PrintWriter(bufferedWriter, autoFlush)) {
            for (int i = 0; i < NUMBER_OF_LINES; i++) {
                var lineNumber = i + 1;
//                String content = "%09d: this is a line of dummy data".formatted(lineNumber);
//                String content = String.format("%09d: this is a line of dummy data", lineNumber);
                String content = formatter.format("%09d: this is a line of dummy data", lineNumber).toString();
                writer.println(content);
            }
        }

        log.info("Generated a large file to {}", filePath);
        return file;
    }

    /**
     * Write a hardcoded series of messages to a file, one by one. Then print out its contents.
     * @param openOption the given OpenOption will be used for the OutputStream.
     */
    private static File writeToFile(OpenOption openOption) throws IOException {
        var file = new File(tempDir, TEMP_FILE);
        createFileFresh(file);
        var filePath = file.toPath();


        // Write the content
        var messages = List.of("Hello,\n", "world", "!\n");
        for (String message : messages) {
            Files.writeString(filePath, message, openOption);
        }

        // Read the content
        var foundContent = Files.readString(filePath);
        log.info("All messages were written to the file. The files contents:\n{}", foundContent);

        return file;
    }

    /**
     * Write to a file and use the 'APPEND' {@link OpenOption}.
     */
    private static File writeToFileAppend() throws IOException {
        log.info("Will write to a file using the 'APPEND' OpenOption. The content from all write operations will be in the file");
        return writeToFile(StandardOpenOption.APPEND);
    }

    /**
     * Write to a file and use the 'TRUNCATE_EXISTING' {@link OpenOption}.
     *
     * Note: if we were to omit any options to the the `Files.writeString` method, then it would actually default to
     * using 'TRUNCATE_EXISTING'. So, we are being unnecessarily explicit. See https://github.com/openjdk/jdk/blob/97c99b5d7d4fc057a7ebc378d1e7dd915eaf0bb3/src/java.base/share/classes/java/nio/file/spi/FileSystemProvider.java#L427
     */
    private static File writeToFileTruncate() throws IOException {
        log.info("Will write to a file using the 'TRUNCATE_EXISTING' OpenOption. Only the content of the last write operation will be in the file");
        return writeToFile(StandardOpenOption.TRUNCATE_EXISTING);
    }
}
