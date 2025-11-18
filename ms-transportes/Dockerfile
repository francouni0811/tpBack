FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . /workspace
RUN mvn -DskipTests package --no-transfer-progress

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copia el JAR construido desde la etapa de build
COPY --from=build /workspace/target/*.jar app.jar
# Copiar propiedades específicas para el perfil 'docker' fuera del JAR para que Spring las encuentre en /config
COPY src/main/resources/application-docker.properties /config/application-docker.properties
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
