FROM gradle:8.12.0-jdk23-corretto AS build

WORKDIR /app
RUN git clone https://github.com/JacksonKuo/app-springboot.git .
RUN chmod +x gradlew
RUN ./gradlew build --info -Dorg.gradle.jvmargs="-Xmx512m -XX:MaxMetaspaceSize=512m" 