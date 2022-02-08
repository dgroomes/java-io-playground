package dgroomes;

/**
 * Types of compression algorithms.
 */
public enum CompressionType {
    NONE(""),
    GZIP(".gz"),
    ZLIB(".zz");
    // TODO support brotli (?), and probably ZSTD

    public final String fileExtension;

    CompressionType(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
