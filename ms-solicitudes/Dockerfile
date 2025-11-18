FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace
# Copia todo el módulo y construye el JAR dentro de la imagen
COPY . /workspace
RUN mvn -DskipTests package --no-transfer-progress

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copia el JAR construido desde la etapa de build
COPY --from=build /workspace/target/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
