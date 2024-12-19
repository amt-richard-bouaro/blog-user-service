FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: Create the Docker image
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp

# Install curl for health checks
RUN apk add --no-cache curl

COPY --from=build /workspace/target/*.jar /app/blog-user-management.jar
ENTRYPOINT ["java", "-jar","/app/blog-user-management.jar"]