# Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn -DskipTests clean package

# Run Stage 
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /target/*.jar app.jar

# Set Port 3000
ENV PORT 3000
EXPOSE 3000

# Start Application
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT} -jar /app/app.jar"]