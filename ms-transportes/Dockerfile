FROM eclipse-temurin:21-jre
WORKDIR /app
# Espera un JAR preconstruido en target/ (construya localmente con `mvn -DskipTests package`)
COPY target/*.jar app.jar
# Copiar propiedades específicas para el perfil 'docker' fuera del JAR para que Spring las encuentre en /config
COPY src/main/resources/application-docker.properties /config/application-docker.properties
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
