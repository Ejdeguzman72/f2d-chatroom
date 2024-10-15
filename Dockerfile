FROM arm64v8/openjdk:17-jdk

WORKDIR /app

COPY target/chatroom-0.0.1-SNAPSHOT.jar /app/chatroom-0.0.1-SNAPSHOT.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/chatroom-0.0.1-SNAPSHOT.jar"]