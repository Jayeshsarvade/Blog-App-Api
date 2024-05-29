# Use the official OpenJDK 17 base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/user-address-service-blogappapi.jar /app

# Expose the port your app runs on
EXPOSE 7070

# Command to run the application
CMD ["java", "-jar", "user-address-service-blogappapi.jar"]
