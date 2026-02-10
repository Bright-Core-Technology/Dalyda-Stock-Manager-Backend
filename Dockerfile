# Use Maven to build the application
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage - use JRE only (smaller image)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Set Spring Boot test profile for CI/CD; no .env file needed, secrets via GitHub Actions
ENV SPRING_PROFILES_ACTIVE=test

# Copy the JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]