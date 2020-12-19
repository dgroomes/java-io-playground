package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Generating test data that we will later use to exercise Java IO APIs.
 *
 * TODO: why is this so slow to execute? It takes a few minutes just to generate around 4GB. Because the println flushes
 *  probably, right?
 */
public class TestDataGenerator {

    private static final Logger log = LoggerFactory.getLogger(TestDataGenerator.class);
    private static final String TEMP_FILE = "large-temp-file.txt";
    private static final String TEMP_DIR = "tmp";
    private static final int NUMBER_OF_LINES = 100_000_000;

    /**
     * Generate a large file of test data
     */
    public File generateLargeFile() {
        var tempDir = new File(TEMP_DIR);
        if (tempDir.mkdir()) {
            log.debug("Created the temp directory");
        }
        var file = new File(tempDir, TEMP_FILE);
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

        return file;
    }


}
