# Use OpenJDK 17 slim image
FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=target/hrms-authentication-1.0.0.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
