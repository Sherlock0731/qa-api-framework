FROM maven:3.9.6-eclipse-temurin-17-alpine

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Download dependencies
RUN mvn dependency:go-offline -B

# Set environment variables
ENV MAVEN_OPTS="-Xmx1024m"

# Default command
CMD ["mvn", "clean", "test", "-Dthread.count=4"]
