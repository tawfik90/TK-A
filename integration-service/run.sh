#!/bin/sh

sh ../maven/bin/mvn clean package && java -jar target/integration-service-0.0.1-SNAPSHOT.jar
