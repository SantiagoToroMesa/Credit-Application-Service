# ========================================================================
# === STAGE 1: build (Compilación)
# ========================================================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia pom.xml y descarga dependencias para aprovechar el cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el código fuente y compila el JAR, saltando las pruebas
COPY src ./src
RUN mvn clean package -DskipTests
# El JAR queda en /app/target/credit-application-service-0.0.1-SNAPSHOT.jar


# ========================================================================
# === STAGE 2: production (Ejecución)
# ========================================================================
# Usamos JRE slim (más pequeño que el JDK completo)
FROM eclipse-temurin:21-jre-alpine AS run

# Establece la zona horaria (ejemplo)
ENV TZ=America/Bogota

WORKDIR /app

# Copia el JAR final de la etapa 'build'
COPY --from=build /app/target/credit-application-service-0.0.1-SNAPSHOT.jar /app/app.jar

# Puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]