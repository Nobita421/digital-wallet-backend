# Build stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn ./.mvn
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/digital-wallet-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]