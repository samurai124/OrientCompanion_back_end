# -------------------------------------------------------------
# Stage 1: Build the Spring Boot application using Maven
# -------------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copy pom.xml and cache dependencies layer
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and package application
COPY src ./src
RUN mvn clean package -DskipTests -B

# -------------------------------------------------------------
# Stage 2: Lightweight runtime image
# -------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root system user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy executable jar from builder stage
COPY --from=builder /build/target/*.jar app.jar
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Expose Spring Boot server port
EXPOSE 8080

# Configure JVM flags optimized for container environments
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
