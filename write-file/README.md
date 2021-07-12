# write-file

A simple program that illustrates a few different ways to write to a file using the Java standard libraries.

## Pre-requisites

* Java 16 must be installed
  * The program depends on this version. It is specified in the `build.gradle.kts` file using the `java { ... }` configuration
    block which implements Gradle's [Toolchains for JVM projects feature](https://docs.gradle.org/current/userguide/toolchains.html). 

## Instructions

Follow these instructions to build and run the program:

1. Use Java 8 or higher
   * Note: Gradle runs well on Java 8 and can be unstable on the very latest versions of Java. 
1. Build and run the program:
   * `./gradlew run --args append`
       * This mode will append lines to a file. 
   * Alternatively, try the other modes given by the following commands.
   * `./gradlew run --args truncate`
       * This mode will truncate a file before writing to it.
   * `./gradlew run --args large`
       * This mode will generate a large file to `tmp/large-file.txt`.
   * `./gradlew run --args substitutions`
       * This mode will copy the `README.md` file to a temp file and make some string substitutions.
