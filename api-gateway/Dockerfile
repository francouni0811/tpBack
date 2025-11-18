FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . /workspace
RUN mvn -DskipTests package --no-transfer-progress

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copia el JAR construido desde la etapa de build
COPY --from=build /workspace/target/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]