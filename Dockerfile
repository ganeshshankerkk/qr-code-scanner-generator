FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/qr-code.jar qr-code.jar
ENTRYPOINT ["java", "-jar", "/qr-code.jar"]