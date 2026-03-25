# Build stage (Maven + JDK 11)
FROM maven:3.9-eclipse-temurin-11-alpine AS build
WORKDIR /app

# Copy pom and download dependencies (cached unless pom changes)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn package -DskipTests -B

# Run stage
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -g 1000 app && adduser -u 1000 -G app -D app
USER app

COPY --from=build /app/target/*.jar app.jar

# Set cloud profile by default when running in container; override with env
ENV SPRING_PROFILES_ACTIVE=cloud
ENV PORT=8081
EXPOSE 8081

# JWT_SECRET and optionally SPRING_DATASOURCE_* must be set at runtime
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
