# --- Stage 1: Build the application ---
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Run the application ---
# Use the correct tag: 'openjdk:17-alpine' (usually implies the JDK is included)
# OR use a provider-specific image that is actively maintained
FROM openjdk:17-alpine
# Alternative images you can use:
# FROM amazoncorretto:17-alpine-jdk
# FROM bellsoft/liberica-openjre-alpine-musl:17

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]