#!/bin/sh

sh ../maven/bin/mvn clean package && java -jar target/eureka-server-0.0.1-SNAPSHOT.jar
