FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy everything and build the JAR
COPY . .
RUN ./mvnw clean package -DskipTests

# ---- Production image ----
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar application.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
