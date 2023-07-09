FROM maven:3.8.5-openjdk-8 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:8-jdk-alpine
COPY --from=build /target/qr.code-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]