FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/qr.code-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]