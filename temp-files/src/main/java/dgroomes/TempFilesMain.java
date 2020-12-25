package dgroomes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TempFilesMain {

    private static final Logger log = LoggerFactory.getLogger(TempFilesMain.class);

    public static void main(String[] args) throws IOException {
        Path tempDirPath = Files.createTempDirectory("dgroomes-temp-files");
        File tempDir = tempDirPath.toFile();
        tempDir.deleteOnExit();

        var tempFiles = List.of(tempFile(tempDirPath, "some-data"),
                tempFile(tempDirPath, "some-additional-data"),
                tempFile(tempDirPath, "some-more-data"));

        log.info("Created {} temp files", tempFiles.size());
    }

    /**
     * Create a temporary file. It should be deleted when the JVM exits.
     *
     * @param dir the directory in which to create the temp file
     * @param prefix the temp file name will include this prefix
     * @return a Path object representing the path to the file
     */
    private static Path tempFile(Path dir, String prefix) throws IOException {
        Path filePath = Files.createTempFile(dir, prefix, "txt");
        log.info("Created temporary file {}", filePath);
        filePath.toFile().deleteOnExit();
        return filePath;
    }
}
