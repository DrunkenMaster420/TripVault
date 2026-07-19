# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/TripVault-0.0.1-SNAPSHOT.jar app.jar

# Informational only for Render
EXPOSE 8080

# UPDATED: Explicitly passes the dynamic port variable to the JVM process
ENTRYPOINT ["sh", "-c", "java -XX:+UseContainerSupport -Dserver.port=${PORT:-8080} -jar app.jar"]