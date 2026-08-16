# ===================================================================
# Multi-stage Dockerfile for JobTrack Spring Boot Backend
# Java 21 LTS + Eclipse Temurin Alpine Linux
# ===================================================================

# -------------------------------------------------------------------
# Stage 1: Build & Package the Application
# -------------------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Optimize caching: Download Maven dependencies first
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy application source code and compile production JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# -------------------------------------------------------------------
# Stage 2: Production Lightweight JRE Runtime
# -------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine AS runner

# Install wget & curl for healthcheck probes
RUN apk add --no-cache wget curl

# Create non-root service user for enhanced container security
RUN addgroup -S jobtrack && adduser -S jobtrack -G jobtrack

WORKDIR /app

# Copy built artifact from builder stage
COPY --from=builder /app/target/jobtrack-backend-*.jar app.jar

# Set permissions
RUN chown -R jobtrack:jobtrack /app

# Expose backend REST API port
EXPOSE 8080

# Switch to non-root user
USER jobtrack

# Container healthcheck using Spring Boot Actuator
HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

# Optimized JVM memory and security flags for containerized workload
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
