# syntax=docker/dockerfile:1

# ---- Build stage: compile the Spring Boot jar ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Cache dependencies first so source-only changes don't re-download the world.
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- Runtime stage: slim JRE, non-root ----
FROM eclipse-temurin:17-jre
WORKDIR /app
RUN useradd -r -u 1001 spring && mkdir -p /app/uploads && chown -R spring /app
COPY --from=build /app/target/*.jar app.jar
USER spring
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
