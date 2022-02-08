# read-file

A simple program that illustrates a few different ways to read from a file using the Java standard libraries.

## Instructions

Follow these instructions to build and run the program.

1. Use Java 17
2. Build and run the program:
   * `./gradlew run --args "README.md buffered"`
   * It will read the `README.md` file.
3. Next, try to read a different file
   * Instead of `README.md`, try a larger file. You can use the `write-file` sub-project to generate a large file.
4. Next, try a different read technique
   * There are other options besides `buffered`. See the enum `ReadTechnique.java` for more information.
