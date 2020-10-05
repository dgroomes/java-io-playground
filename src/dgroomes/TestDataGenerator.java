package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Generating test data. In particular, this class generates a large file that we will use to exercise Java IO
 * operations. For example, for a file that is 4GB large we can't afford to read it into memory so we better use
 * buffering. How do we buffer when reading a file using Java's standard libraries. How many *different* ways are there
 * to do this? What's the most idiomatic way to do this?
 */
public class TestDataGenerator {

    private static final Logger log = LoggerFactory.getLogger(TestDataGenerator.class);
    private static final int NUMBER_OF_LINES = 100_000_000;

    public File generateLargeFile() {
        var tempDir = new File("tmp");
        if (tempDir.mkdir()) {
            log.debug("Created the temp directory");
        }
        var file = new File(tempDir,"large-temp-file.txt");
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
