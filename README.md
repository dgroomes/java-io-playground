# java-io-playground

NOT YET IMPLEMENTED

📚 Learning and exploring Java's IO (input/output) standard libraries.

---

### Learning about Java IO

My goal is to learn more about Java IO. Specifically:

* I'm refreshing some of my existing Java IO knowledge
* I want to learn stuff that is old but new to me
  * In particular, I want to learn about Java's "new IO" in the `java.nio` which apparently has been controversial from
    its inception. I'd like to form my own opinions about that by actually trying it out. Again, this is not new stuff!
* I want to learn new stuff (Java 11+)

Referenced material:

* The official Java language tutorials  <https://docs.oracle.com/javase/tutorial/essential/io/file.html>

### Instructions

NOTE: this was developed on macOS.

1. Use Java 15
1. Execute `./build.sh` to compile the source code. NOTE: the first time you do this you'll want to uncomment `generateTestData()`
   so that the test data is generated. You don't want to do this everytime because it's slow and will wear down your disk unnecessarily. 
1. Execute `./run.sh` to run the program (i.e. the `main` method).
