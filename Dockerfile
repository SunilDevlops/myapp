# Use Ubuntu as the base image
FROM ubuntu:20.04

# Set the working directory inside the container
WORKDIR /app

# Install OpenJDK-17 on the Ubuntu image
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    rm -rf /var/lib/apt/lists/*

# Copy the packaged Spring Boot JAR file into the container
COPY target/myapp-1.0.jar /app/myapp-1.0.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Command to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/myapp-1.0.jar"]