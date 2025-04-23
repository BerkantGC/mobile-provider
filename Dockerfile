# Use Eclipse Temurin OpenJDK 21 runtime image
FROM eclipse-temurin:21-jre

# Argument for the JAR file
ARG JAR_FILE=target/*.jar

# Copy the application JAR to the container
COPY ${JAR_FILE} application.jar

# Expose application port (optional, if your app runs on a specific port like 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "application.jar"]