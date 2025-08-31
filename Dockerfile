# Use Java 17 runtime
FROM eclipse-temurin:17-jdk-alpine

# Install Maven
RUN apk add --no-cache maven

# Set working dir
WORKDIR /app

# Copy pom.xml and source
COPY pom.xml ./
COPY src src

# Build the app
RUN mvn -f pom.xml clean package -DskipTests

# Expose app port
EXPOSE 8080

# Run app
ENTRYPOINT ["java","-jar","target/tinyurl-0.0.1-SNAPSHOT.jar"]
