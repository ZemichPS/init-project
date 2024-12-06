FROM openjdk:21-bookworm
WORKDIR /app
COPY build/libs/*.jar /app/user-ms.jar
EXPOSE 64102
CMD ["java", "-jar", "user-ms.jar"]
