FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/chatroom-0.0.1-SNAPSHOT.jar /app/chatroom-0.0.1-SNAPSHOT.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/chatroom-0.0.1-SNAPSHOT.jar"]