# 001_setup

This project is called:  poc_threshold_signing_single_thread

## Overview

This project is a poc showing how threshold signing works.  It will include methods that serialize and deserialize arguments.  There will be no network in between the serializing and deserializing and it will run in a single thread.  

However as there are serialization and deserialization methods it will not be hard to later use this poc as the starting point for another poc which does use networks.

## Dependencies

I want you to create a Java Spring Boot project using: 

* java 20
* maven
* lombok
* sl4j using log4j
* junit

I want the pom to also include these libraries:

```
<dependency>
    <groupId>com.weavechain</groupId>
    <artifactId>threshold-sig</artifactId>
    <version>1.3</version>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>com.weavechain</groupId>
    <artifactId>curve25519-elisabeth</artifactId>
    <version>0.1.5</version>
</dependency>
```

I think we need to exclude slf4j-api from threshold-sig but am not totally sure.


## properties

I want you to create an application.yaml which has a "dev" profile in it.

I want one property in it called:

```
message
```

I want the value of message to be:

```
Hello World!
```


## Code

I want you to create a start class:

```
com.thresholdsign.start.Start
```

The Start class should have a start() method.  This start() method should run after Spring Boot has started up and dependency injection has had a chance to complete.

For now I want the start() method to just to log a message which was read from the application.yaml file.  The property to read from the yaml file is the message.


## Junit Test Code

I want you to create an example junit test called:

```
com.thresholdsign.test.calculate.AdditionTest.testAddition()
```

This should exist under the src/test/java source tree

I want the test code to just assert that 2 + 2 = 4


## Scripts

I want you to create a shell script under a scripts directory called:

```
start.sh
```

Inside start.sh I want it to run the Spring Boot application.  Running start.sh should result in "Hello World!" being written to the console from com.thresholdsign.start.Start.start().

I also want you to create a shell script called:

```
test.sh 
```

Inside test.sh I want it to run the Junit Test code for the above mentioned:

```
com.thresholdsign.test.calculate.AdditionTest.testAddition()
```

## .gitignore

I want you to create a .gitignore file which also stops eclipse / STS / intelij specific files being added to git.

I also do not want mac specific files like .DS_Store to be added to git

## README

I want you to create a README which includes the information under "Overview" above and also has instructions for running the shell scripts.

I also want the README to explain how to configure STS to use Lombok


## Testing

I want you to generate all of the above and then call the above specified shell script to make sure you have completed your job well.

