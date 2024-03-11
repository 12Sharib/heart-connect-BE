# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk

WORKDIR /app
COPY ./target/HeartConnect-1.0.0.jar /app
EXPOSE 9091
CMD ["java", "-jar", "HeartConnect-1.0.0.jar"]

