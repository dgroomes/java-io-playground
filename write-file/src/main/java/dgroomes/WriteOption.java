package dgroomes;

/**
 * This enum models the different options that the program takes.
 */
public enum WriteOption {

    LARGE,
    TRUNCATE,
    APPEND,
    SUBSTITUTIONS,
    COMPRESS_GZIP,
    COMPRESS_ZLIB,
    COMPRESS_ZSTD,
    COMPRESS_ZSTD_STRONGEST,
    COMPRESS_NONE
}
