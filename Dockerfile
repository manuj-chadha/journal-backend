# Use OpenJDK as base
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file built by Maven/Gradle
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar


# Expose Spring Boot default port
EXPOSE 8080

# Entry point
ENTRYPOINT ["java", "-jar", "app.jar"]