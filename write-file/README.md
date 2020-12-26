# write-file

A simple program that illustrates a few different ways to write to a file using the Java standard libraries.

## Pre-requisites

* Java 15 must be installed
  * The program depends on this version. It is specified in the `build.gradle.kts` file using the `java` configuration
    block which implements Gradle's [Toolchains for JVM projects feature](https://docs.gradle.org/current/userguide/toolchains.html). 

## Instructions

1. Use Java 8 or higher (Gradle depends on this version of Java or higher)
1. Execute `./gradlew run --args append` to run the program. Alternatively, try the other options:
   * `./gradlew run --args truncate`.
   * `./gradlew run --args large`. It will generate a large file to `tmp/large-temp-file.txt`.
