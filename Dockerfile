# --- Stage 1: Build the application ---
# This stage uses a maven image to compile our code
FROM maven:3.8.1-openjdk-17 AS build
# 'app' is the name of the directory where our source code lives inside THIS container
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
# This build step creates a file like: /app/target/my-inventory-0.0.1-SNAPSHOT.jar


# --- Stage 2: Run the application ---
# This stage uses a clean, light base image
FROM openjdk:17-jdk-alpine
# 'app' is also the name of the directory where the final JAR will live inside the FINAL container
WORKDIR /app

# Copy the file created in Stage 1's 'target' folder, and rename it to 'app.jar'
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run the final executable file named 'app.jar'
ENTRYPOINT ["java", "-jar", "app.jar"]