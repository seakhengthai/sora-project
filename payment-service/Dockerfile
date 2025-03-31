FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/payment-service.jar app.jar
EXPOSE 9092
ENTRYPOINT ["java", "-jar", "app.jar"]