# ── Build stage ───────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn -DskipTests clean package

# ── Run stage ─────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
EXPOSE 3000
COPY --from=build /target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]