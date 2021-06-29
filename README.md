# java-io-playground

📚 Learning and exploring Java's IO (input/output) standard libraries.

---
**NOTE**:

This was developed on macOS and for my own personal use.

---

## Learning about Java IO

My goal is to learn more about Java IO. Specifically:

* I'm refreshing some of my existing Java IO knowledge
* I want to learn stuff that is old but new to me
  * In particular, I want to learn about Java's "new IO" in the `java.nio` which apparently has been controversial from
    its inception. I'd like to form my own opinions about that by actually trying it out. Again, this is not new stuff!
* I want to learn new stuff (Java 11+)

## Standalone sub-projects

This repository illustrates different concepts, patterns and examples via standalone sub-projects. Each sub-project is
completely independent of the others and do not depend on the root project. This _standalone sub-project constraint_
forces the sub-projects to be complete and maximizes the reader's chances of successfully running, understanding, and
re-using the code.

The sub-projects include:

### `read-file/`

A simple program that illustrates a few different ways to read from a file using the Java standard libraries.

See the README in [read-file/](read-file/).

### `write-file/`

A simple program that illustrates a few different ways to write to a file using the Java standard libraries.

See the README in [write-file/](write-file/).

### `temp-files/`

A simple program that creates temporary files and directories using the Java standard libraries.

See the README in [temp-files/](temp-files/).

## Referenced material

* The [official Java language tutorials](https://docs.oracle.com/javase/tutorial/essential/io/file.html)
* [`gradle-playground` GitHub repo](https://github.com/dgroomes/gradle-playground)
