FROM openjdk:17-jdk-slim

WORKDIR /app
COPY . /app
RUN chmod +x gradlew
RUN ./gradlew build --scan --build-cache --info --console=plain -Dorg.gradle.jvmargs="-Xmx512m -XX:MaxMetaspaceSize=512m" 
EXPOSE 8080
CMD ["java", "-jar", "build/libs/sample-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=local"]