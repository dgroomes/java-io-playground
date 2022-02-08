package dgroomes;

/**
 * Types of compression algorithms.
 */
public enum CompressionType {
    NONE(""),
    GZIP(".gz"),
    ZLIB(".zz"),
    ZSTD(".zst"),
    ZSTD_STRONGEST(".zst");

    public final String fileExtension;

    CompressionType(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
