#!/bin/sh
set -e  # Exit immediately on error

exec java $JAVA_OPTS -jar sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=$SPRING_PROFILE
