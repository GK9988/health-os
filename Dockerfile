# ──────────────────────────────────────────────
# Stage 1: Build
# ──────────────────────────────────────────────
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

# Cache Gradle wrapper + dependencies first
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle/ gradle/

# Copy all subproject build files (for dependency resolution)
COPY service-discovery/build.gradle   service-discovery/build.gradle
COPY config-server/build.gradle       config-server/build.gradle
COPY api-gateway/build.gradle         api-gateway/build.gradle
COPY workout-service/build.gradle     workout-service/build.gradle
COPY sleep-service/build.gradle       sleep-service/build.gradle
COPY nutrition-service/build.gradle   nutrition-service/build.gradle
COPY supplement-service/build.gradle  supplement-service/build.gradle
COPY ai-service/build.gradle          ai-service/build.gradle

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy full source
COPY . .

# Build the target service's bootJar
ARG SERVICE_NAME
RUN ./gradlew :${SERVICE_NAME}:bootJar --no-daemon -x test

# ──────────────────────────────────────────────
# Stage 2: Slim runtime image
# ──────────────────────────────────────────────
FROM eclipse-temurin:25-jre

WORKDIR /app

# Add curl for health checks
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*


ARG SERVICE_NAME
COPY --from=builder /app/${SERVICE_NAME}/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
