FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/user-profile-service.jar app.jar
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]