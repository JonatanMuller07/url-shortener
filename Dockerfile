FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Usar una imagen de OpenJDK para la aplicación
FROM openjdk:17-jdk-slim
COPY --from=build /target/url-shortener-0.0.1-SNAPSHOT.jar app-url-shortener.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app-url-shortener.jar"]