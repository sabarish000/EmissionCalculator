#!/bin/sh

# Run the Java application with the provided arguments
java -jar "$(dirname "$0")/target/Co2EmissionCalculator-1.0-SNAPSHOT.jar" "$@"