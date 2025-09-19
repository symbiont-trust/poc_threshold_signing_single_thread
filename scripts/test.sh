#!/bin/bash

# Set JAVA_HOME to Java 20
export JAVA_HOME=$(/usr/libexec/java_home -v 20)

# Run the specific JUnit test
echo "Running AdditionTest..."
mvn test -Dtest=com.thresholdsign.test.calculate.AdditionTest#testAddition