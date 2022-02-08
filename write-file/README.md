# write-file

A simple program that illustrates a few different ways to write to a file using the Java standard libraries.

## Instructions

Follow these instructions to build and run the program:

1. Use Java 17
2. Build and run the program:
   * `./gradlew run --args append`
   * This mode will append lines to a file. 
3. Try a different write mode:
   * `./gradlew run --args truncate`
   * This mode will truncate a file before writing to it.
4. Create a large file:
   * `./gradlew run --args large`
   * This mode will generate a large file to `tmp/large-file.txt`.
5. Copy a file and make some substitution
   * `./gradlew run --args substitutions`
   * This mode will copy the `README.md` file to a temp file and make some string substitutions.
6. Compress data and write it to a file:
   * `./gradlew run --args compress_gzip`
   * Peek at the file contents with the following command.
   * `gzip -cd tmp/file.txt.gz | head`
7. Use a different compression algorithm:
   * `./gradlew run --args compress_zlib`
   * Peek at the file contents with the following command.
   * `perl -MCompress::Zlib -e 'undef $/; print uncompress(<>)' < tmp/file.txt.zz | head`

## Wish list

General clean ups, TODOs and things I wish to implement for this project:

* DONE Use compression (like gzip)
* DONE Use another compression type. Does anything offer parallelization?

## Reference

* [JavaDoc: java.util.zip.Deflater](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/zip/Deflater.html)
