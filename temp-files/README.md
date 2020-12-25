# temp-files

A simple program that creates temporary files and directories using the Java standard libraries.

## Pre-requisites

* Java 15 must be installed
  * The program depends on this version. It is specified in the `build.gradle.kts` file using the `java` configuration
    block which implements Gradle's [Toolchains for JVM projects feature](https://docs.gradle.org/current/userguide/toolchains.html). 

## Instructions

1. Use Java 8 or higher (Gradle depends on this version of Java or higher)
1. Execute `./gradlew run` to run the program. It will create some temp files and delete them.

### Referenced material

* [The Java Tutorials: *Reading, Writing, and Creating Files*](https://docs.oracle.com/javase/tutorial/essential/io/file.html)
