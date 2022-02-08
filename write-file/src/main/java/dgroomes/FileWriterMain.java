package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * Write to files. See the README.md for more information.
 */
public class FileWriterMain {

    private static final Logger log = LoggerFactory.getLogger(FileWriterMain.class);
    private static final String TEMP_LARGE_FILE = "large-file.txt";
    private static final String TEMP_FILE = "file.txt";
    private static final String TEMP_DIR = "tmp";
    private static final int LARGE_FILE_NUMBER_OF_LINES = 100_000_000;
    private static final int COMPRESSED_DATA_FILE_NUMBER_OF_LINES = 1_000_000;
    private static File tempDir;

    public static void main(String[] args) throws IOException {
        int numArgs = args.length;
        if (numArgs != 1) {
            throw new IllegalArgumentException("Expected exactly one argument but found %d".formatted(numArgs));
        }

        var option = validateOptionArg(args[0]);

        tempDir = new File(TEMP_DIR);
        if (tempDir.mkdir()) {
            log.info("Created the temp directory");
        }

        WriteOption _ignoredReturnValueToForceExhaustivenessInSwitch = switch (option) {
            case LARGE -> generateLargeFile();
            case APPEND -> writeToFileAppend();
            case TRUNCATE -> writeToFileTruncate();
            case SUBSTITUTIONS -> copyFileWithSubstitutions();
            case COMPRESS_NONE -> writeToFileWithCompression(CompressionType.NONE);
            case COMPRESS_GZIP -> writeToFileWithCompression(CompressionType.GZIP);
        };
    }

    /**
     * Validate the 'write option' argument and return a WriteOption enum value representing it.
     *
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
     *
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
     * <p>
     * Why is this so slow to execute? It takes a few minutes just to generate around 4GB. Because the println flushes
     * probably, right?
     */
    private static WriteOption generateLargeFile() throws IOException {
        var file = new File(tempDir, TEMP_LARGE_FILE);
        createFileFresh(file);
        var filePath = file.getAbsolutePath();
        log.info("Generating a large file to {}", filePath);

        try (var writer = new PrintWriter(file)) {
            for (int i = 0; i < LARGE_FILE_NUMBER_OF_LINES; i++) {
                var lineNumber = i + 1;
                writer.println("%09d: this is a line of dummy data".formatted(lineNumber));
            }
        } catch (FileNotFoundException e) {
            log.error("Unexpected error when generating large file", e);
            throw new IllegalStateException(e);
        }

        log.info("Generated a large file to {}", filePath);
        return WriteOption.LARGE;
    }

    /**
     * Write a hardcoded series of messages to a file, one by one. Then print out its contents.
     *
     * @param openOption the given OpenOption will be used for the OutputStream.
     */
    private static void writeToFileAFewTimes(OpenOption openOption) throws IOException {
        var file = new File(tempDir, TEMP_FILE);
        createFileFresh(file);
        var filePath = file.toPath();

        // Write the content
        var messages = List.of("Hello,\n", "world", "!\n");
        for (String message : messages) {
            // Normally, when writing data in a loop you would want to open the file just once for efficiency. But this
            // method needs to illustrate how the OpenOptions behave differently over a series of independent write
            // operations. So, purposely use a method like "Files.writeToString" here.
            Files.writeString(filePath, message, openOption);
        }

        // Read the content
        var foundContent = Files.readString(filePath);
        log.info("All messages were written to the file. The files contents:\n{}", foundContent);
    }

    /**
     * Write to a file and use the 'APPEND' {@link OpenOption}.
     */
    private static WriteOption writeToFileAppend() throws IOException {
        log.info("Will write to a file using the 'APPEND' OpenOption. The content from all write operations will be in the file");
        writeToFileAFewTimes(StandardOpenOption.APPEND);
        return WriteOption.APPEND;
    }

    /**
     * Write to a file and use the 'TRUNCATE_EXISTING' {@link OpenOption}.
     * <p>
     * Note: if we were to omit any options to the the `Files.writeString` method, then it would actually default to
     * using 'TRUNCATE_EXISTING'. So, we are being unnecessarily explicit. See https://github.com/openjdk/jdk/blob/97c99b5d7d4fc057a7ebc378d1e7dd915eaf0bb3/src/java.base/share/classes/java/nio/file/spi/FileSystemProvider.java#L427
     */
    private static WriteOption writeToFileTruncate() throws IOException {
        log.info("Will write to a file using the 'TRUNCATE_EXISTING' OpenOption. Only the content of the last write operation will be in the file");
        writeToFileAFewTimes(StandardOpenOption.TRUNCATE_EXISTING);
        return WriteOption.TRUNCATE;
    }

    /**
     * Compress and write data to a file.
     *
     * Interestingly, it's faster to GZIP the data and write it to a file (~ 2 seconds) than it is to do no compression
     * at all (~3.5 seconds). This because the bottleneck is IO not the CPU! This is often an unintuitive reality with
     * a lot of workloads.
     */
    private static WriteOption writeToFileWithCompression(CompressionType compressionType) throws IOException {
        var file = new File(tempDir, TEMP_FILE);
        createFileFresh(file);
        var filePath = file.getAbsolutePath();
        log.info("Compressing lots of content and writing it to a file: '{}'. Using compression type {}", filePath, compressionType);

        Duration duration;

        // Set up the right OutputStream (a normal file; or GZIP compressed; or ... ?). "Coding to interfaces" rocks".
        // Then, write lots of content to the file.
        var fileOutputStream = new FileOutputStream(file);
        try (var outputStream = switch (compressionType) {
            case NONE -> fileOutputStream;
            case GZIP -> new GZIPOutputStream(fileOutputStream);
        }) {

            var start = Instant.now();
            for (int i = 0; i < COMPRESSED_DATA_FILE_NUMBER_OF_LINES; i++) {
                var lineNumber = i + 1;
                var message = "%09d: this is a line of dummy data".formatted(lineNumber);
                byte[] bytes = message.getBytes();
                outputStream.write(bytes);
            }
            var end = Instant.now();
            duration = Duration.between(start, end);
        }

        String fileSizeBytesPretty = NumberFormat.getInstance().format(file.length());
        log.info("""
            Done compressing data and writing it to the file.
                The file's final size in bytes: {}
                Duration: {}""", fileSizeBytesPretty, duration);

        return WriteOption.COMPRESS_GZIP;
    }

    /**
     * Copy the content from a "source" file into a "target" file while making some substitutions for some tokens.
     * Specifically, copy the "README.md" file into a temp directory and make the following substitutions:
     * - "Pre-requisites" is replaced with "Requirements"
     * - "program" is replaced with "script"
     */
    private static WriteOption copyFileWithSubstitutions() throws IOException {
        var sourceFile = new File("README.md");
        var targetFile = new File(tempDir, "README.md");
        createFileFresh(targetFile);
        log.info("Copying file '{}' to '{}' and making substitutions", sourceFile.getAbsolutePath(), targetFile.getAbsolutePath());


        try (var reader = new BufferedReader(new FileReader(sourceFile));
             var writer = new PrintWriter(targetFile)) {

            String line;
            while ((line = reader.readLine()) != null) {

                var lineWithSubs = line
                        .replaceAll("Pre-requisites", "Requirements")
                        .replaceAll("program", "script");

                writer.println(lineWithSubs);
            }
        }

        return WriteOption.SUBSTITUTIONS;
    }
}
