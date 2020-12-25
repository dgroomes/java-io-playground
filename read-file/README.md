# read-file

A simple program that illustrates a few different ways to read from a file using the Java standard libraries.

## Pre-requisites

* Java 15 must be installed
  * The program depends on this version. It is specified in the `build.gradle.kts` file using the `java` configuration
    block which implements Gradle's [Toolchains for JVM projects feature](https://docs.gradle.org/current/userguide/toolchains.html). 

## Instructions

1. Use Java 8 or higher (Gradle depends on this version of Java or higher)
1. Execute `./gradlew run --args "README.md BUFFERED"` to run the program. It will read the `README.md` file.
    1. Suggestion: instead of `README.md`, try a larger file. You can use the `write-file` sub-project to generate a
       large file.
    1. Try different read techniques besides `BUFFERED`. See the enum `ReadTechnique.java` for more information.
