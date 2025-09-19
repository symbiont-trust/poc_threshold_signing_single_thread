# POC Threshold Signing Single Thread

## Overview

This project is a POC showing how threshold signing works. It will include methods that serialize and deserialize arguments. There will be no network in between the serializing and deserializing and it will run in a single thread.

However as there are serialization and deserialization methods it will not be hard to later use this POC as the starting point for another POC which does use networks.

## Requirements

- Java 20
- Maven 3.6+

## Running the Application

### Start the Application

To run the Spring Boot application:

```bash
./scripts/start.sh
```

This will start the application and you should see "Hello World!" logged to the console.

### Run Tests

To run the JUnit tests:

```bash
./scripts/test.sh
```

This will execute the AdditionTest to verify that 2 + 2 = 4.

## IDE Setup

### Configuring Spring Tool Suite (STS) to use Lombok

1. **Download Lombok JAR**:
   - Go to https://projectlombok.org/download
   - Download the latest lombok.jar

2. **Install Lombok in STS**:
   - Run `java -jar lombok.jar`
   - The installer will detect your STS installation
   - Click "Install/Update" and restart STS

3. **Alternative Manual Installation**:
   - Copy lombok.jar to your STS installation directory
   - Add `-javaagent:lombok.jar` to the `SpringToolSuite4.ini` file
   - Restart STS

4. **Verify Installation**:
   - Create a class with Lombok annotations (@Getter, @Setter, etc.)
   - STS should recognize the generated methods without compilation errors

### Project Import

1. Open STS
2. File → Import → Existing Maven Projects
3. Browse to the project directory
4. Select the pom.xml file
5. Click "Finish"

The project should import successfully with all dependencies resolved.

## Dependencies

This project uses:

- Spring Boot 3.1.5
- Java 20
- Maven
- Lombok
- SLF4J with Log4J2
- JUnit 5
- Weavechain Threshold Signing libraries:
  - threshold-sig 1.3
  - curve25519-elisabeth 0.1.5

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/thresholdsign/
│   │       ├── Application.java
│   │       └── start/
│   │           └── Start.java
│   └── resources/
│       └── application.yaml
└── test/
    └── java/
        └── com/thresholdsign/test/calculate/
            └── AdditionTest.java
```