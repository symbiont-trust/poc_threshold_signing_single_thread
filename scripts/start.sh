#!/bin/bash

# Set JAVA_HOME to Java 20
export JAVA_HOME=$(/usr/libexec/java_home -v 20)

# Start the Spring Boot application
echo "Starting Spring Boot application..."
mvn spring-boot:run