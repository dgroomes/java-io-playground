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
