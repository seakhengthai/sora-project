FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/gateway-service.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]