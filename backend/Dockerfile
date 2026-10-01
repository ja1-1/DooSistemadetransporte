# Construye el jar con el Maven Wrapper del proyecto y lo ejecuta con un JRE 25.
FROM eclipse-temurin:25-jdk AS construccion
WORKDIR /backend
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw -q dependency:go-offline
COPY src src
RUN ./mvnw -q package

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=construccion /backend/target/transporte-0.1.0.jar transporte.jar
ENV SERVER_ADDRESS=0.0.0.0 SERVER_PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "transporte.jar"]
