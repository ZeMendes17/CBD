#!/bin/bash

# Start Cassandra
echo "Starting cqlsh..."
cd ~/Documents/apache-cassandra-4.0.11/bin/
export JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"
./cqlsh