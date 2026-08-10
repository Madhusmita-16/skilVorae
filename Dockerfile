# Multi-stage Dockerfile for SkilVorae
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./maven/apache-maven-3.9.6/bin/mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/skilvorae-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
