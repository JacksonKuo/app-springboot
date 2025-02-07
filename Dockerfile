ARG BASE_IMAGE=openjdk:17-jdk-alpine
FROM ${BASE_IMAGE}

WORKDIR /app
COPY build/libs/sample-0.0.1-SNAPSHOT.jar /app
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 8087

ENV JAVA_OPTS="-Xms128m -Xmx256m"
ENV SPRING_PROFILE="prod"

ENTRYPOINT ["/entrypoint.sh"]