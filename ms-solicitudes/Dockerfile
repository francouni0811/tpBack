FROM eclipse-temurin:21-jre
WORKDIR /app
# Espera un JAR preconstruido en target/ (construya localmente con `mvn -DskipTests package`)
COPY target/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
