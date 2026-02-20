# =========================
# Stage 1: Build
# =========================
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# --- AQUÍ ESTÁ LA CORRECCIÓN ---
COPY build.gradle settings.gradle ./

# Copiamos la carpeta gradle (wrapper)
COPY gradle ./gradle

# Descargamos las dependencias
RUN gradle dependencies --no-daemon

# Copiar el código fuente
COPY src ./src

# Construir el JAR ejecutable
RUN gradle bootJar --no-daemon

# =========================
# Stage 2: Runtime
# =========================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto estándar
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java","-jar","app.jar"]