FROM openjdk:8-jdk-alpine
EXPOSE 8080
COPY --from=build /build/libs/qr.code-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]