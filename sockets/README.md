# sockets

A simple "write and listen" program using sockets.

## Pre-requisites

* Java 16 must be installed
    * The program depends on this version. It is specified in the `build.gradle.kts` file using the `java` configuration
      block which implements
      Gradle's [Toolchains for JVM projects feature](https://docs.gradle.org/current/userguide/toolchains.html).

## Instructions

1. Use Java 8 or higher
    * Note: Gradle depends on this version of Java or higher
1. Build the program:
    * `./gradlew install`
1. Run it:
    * `build/install/sockets/bin/sockets`
1. Write a message
    * Write a message in the terminal.
    * You should see that same message logged by the server!
    * It will look something like this:
    ![screenshot.png](screenshot.png)

### Wish list

General clean ups, TODOs and things I wish to implement for this project:

* Create an intermediate process listener, or "forwarder", between the client and LoggingServer that drops vowels
* Periodically fail on the server side. (I'm not sure how to do this; but I want to simulate failure and retries) 
  * DONE First, implement a proper class to abstract the client. This class will later be extended with retry logic.
