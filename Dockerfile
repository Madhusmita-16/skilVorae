# Stage 1: Build with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
ENV MAVEN_OPTS="-Xmx512m"
RUN mvn clean package -DskipTests -q

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Create uploads dir so file upload doesn't fail at startup
RUN mkdir -p /app/uploads

EXPOSE 8080

# Restrict JVM max heap to 350M to fit inside Render's 512M free tier RAM limit
ENTRYPOINT ["java", "-Xmx350m", "-Xms128m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
