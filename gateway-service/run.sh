#!/bin/sh

sh ../maven/bin/mvn clean package && java -jar target/gateway-service-0.0.1-SNAPSHOT.jar
