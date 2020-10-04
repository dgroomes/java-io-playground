#!/usr/bin/env bash
# Compile the Java program.
#
# This script's incantation of the 'javac' command will include libraries in the 'lib/' directory and enable Java
# language preview features.

find src -name "*.java" > sources.txt
javac \
  @sources.txt \
  --class-path "lib/*" \
  -d out \
  --release 15 \
  --enable-preview
