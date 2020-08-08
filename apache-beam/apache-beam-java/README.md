# Try Apache Beam - Java

> Example from: [Try Apache Beam - Java](https://colab.research.google.com/github/apache/beam/blob/master/examples/notebooks/get-started/try-apache-beam-java.ipynb#scrollTo=cPvvFB19uXNw)

## Setup

```sh
# Copy the input file into the local filesystem.
mkdir data/
curl https://storage.googleapis.com/dataflow-samples/shakespeare/kinglear.txt --output ./data/kinglear.txt
```

## Build and run

```sh
# Build the project.
./gradlew clean build

# Check the generated build files.
ls -lh build/libs/

# Run the shadow (fat jar) build.
./gradlew runShadow

# Sample the first 20 results, remember there are no ordering guarantees.
head -n 20 outputs/part-00000-of-*
```

## Distributing your application

We can run our fat JAR file as long as we have a Java Runtime Environment installed.

To distribute, we copy the fat JAR file and run it with `java -jar`.

```sh
# Remove old outputs.
rm -Rf outputs/

# You can now distribute and run your Java application as a standalone jar file.
java -jar build/libs/WordCount.jar

# Sample the first 20 results, remember there are no ordering guarantees.
head -n 20 outputs/part-00000-of-*
```