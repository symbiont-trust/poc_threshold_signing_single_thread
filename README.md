# POC Threshold Signing Single Thread

## Overview

This project is a POC demonstrating threshold signature implementation using Ed25519 cryptography. It simulates a distributed threshold signing system where T-of-N parties collaborate to create a valid signature, but runs in a single thread for simplicity.

The implementation includes serialization and deserialization methods to simulate network communication, making it easy to extend for distributed network-based scenarios.

## Key Features

- **Configurable threshold parameters**: T (threshold) and N (total parties)
- **Flexible node selection**: Specify which nodes participate via configuration
- **Complete threshold signing workflow**: Parameter generation, partial signatures, and final signature combination
- **Signature validation**: Cryptographic verification of the final threshold signature
- **Network simulation**: Serialization/deserialization to emulate distributed communication

## Requirements

- Java 20
- Maven 3.6+

## Configuration

The threshold signing parameters are configured in `src/main/resources/application.yaml`:

```yaml
startup:
    message: "About to run the Threshold Signing Simulation"
    T: 4                    # Threshold: minimum signatures needed
    N: 7                    # Total number of parties
    nodes: "1,3,4,6"       # Comma-separated list of participating nodes
    stringToSign: "test"    # Message to be signed
```

### Configuration Examples

**2-of-3 Threshold:**
```yaml
T: 2
N: 3
nodes: "1,2"
```

**3-of-5 Threshold:**
```yaml
T: 3
N: 5
nodes: "0,2,4"
```

**Important**: The number of nodes specified must equal the threshold T value.

## Running the Application

### Start the Application

To run the Spring Boot application:

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-20.jdk/Contents/Home
mvn spring-boot:run
```

This will execute the threshold signing simulation and display the signature verification result.

### Run Tests

To run the JUnit tests:

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-20.jdk/Contents/Home
mvn test
```

This will execute:
- **ScalarTest**: Tests cryptographic scalar serialization
- **SimpleThresholdTest**: Tests basic threshold signature workflow

## Sample Output

When running successfully, you should see output similar to:

```
INFO c.thresholdsign.start.Start : About to run the Threshold Signing Simulation
INFO c.t.h.t.ThresholdSignerHelperImpl : Sending serialzableParams to node, 1
INFO c.t.h.t.ThresholdSignerHelperImpl : Node with nodeId, 1, Received seriaized NodeParams
...
INFO c.thresholdsign.simulator.SimulatorImpl : Final Signature is valid = true

=== Signature Details ===
Signature length: 64 bytes
Signature (hex): 4a8adaa3b52cfe1ed3ae202c3d2049d9c142214310364560a649e8c0f9f6cde4b5c2372bcac061d52b8bba8e0890c828be35c5e19eeddac1faacdea3ca08e90b
Signature (base64): Sorao7Us/h7TriAsPSBJ2cFCIUMQNkVgpknowPn2zeS1wjcrysBh1SuLuo4IkMgovjXF4Z7t2sH6rN6jygjpCw==
R component (32 bytes): 4a8adaa3b52cfe1ed3ae202c3d2049d9c142214310364560a649e8c0f9f6cde4
S component (32 bytes): b5c2372bcac061d52b8bba8e0890c828be35c5e19eeddac1faacdea3ca08e90b
========================
```

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

## How Threshold Signatures Work

This implementation demonstrates the complete threshold signature workflow:

1. **Parameter Generation**: A coordinator generates threshold parameters including:
   - Public key (shared by all)
   - Private key shares (one per node)
   - Mathematical parameters for the scheme

2. **Round 1 - Commitment Phase**: Each participating node:
   - Receives its private share and the message to sign
   - Computes a commitment value (Ri)
   - Sends the commitment back to the coordinator

3. **Challenge Computation**: The coordinator:
   - Collects all commitments from T nodes
   - Computes the combined commitment (R)
   - Generates the challenge value (k)

4. **Round 2 - Response Phase**: Each node:
   - Receives the challenge value
   - Computes its partial signature using its private share
   - Sends the partial signature to the coordinator

5. **Final Signature**: The coordinator:
   - Combines all T partial signatures
   - Produces the final threshold signature
   - Verifies the signature is valid

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/thresholdsign/
│   │       ├── Application.java                    # Spring Boot main class
│   │       ├── start/
│   │       │   └── Start.java                     # Application startup listener
│   │       ├── simulator/
│   │       │   ├── Simulator.java                 # Simulator interface
│   │       │   └── SimulatorImpl.java             # Main threshold signing coordinator
│   │       └── helper/
│   │           ├── serializer/
│   │           │   ├── NodeParams.java            # Node parameter container
│   │           │   ├── SerializerHelper.java      # Serialization interface
│   │           │   └── SerializerHelperImpl.java  # JSON serialization implementation
│   │           └── thresholdsigner/
│   │               ├── ThresholdSignerHelper.java     # Threshold signer interface
│   │               └── ThresholdSignerHelperImpl.java # Threshold signer implementation
│   └── resources/
│       └── application.yaml                       # Configuration file
└── test/
    └── java/
        └── com/thresholdsign/test/
            ├── calculate/
            │   └── ScalarTest.java                # Cryptographic tests
            └── SimpleThresholdTest.java          # Integration test
```

## Security Considerations

- **Node Authentication**: In production, implement proper node authentication
- **Secure Communication**: Use TLS/SSL for network communication
- **Key Management**: Secure storage and distribution of private shares
- **Replay Protection**: Implement nonce/timestamp mechanisms
- **Audit Logging**: Track all signing operations for security monitoring