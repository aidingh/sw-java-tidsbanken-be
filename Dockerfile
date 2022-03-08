FROM maven:3.8.4-openjdk-17 AS maven

WORKDIR /app
COPY . .
RUN mvn clean install -DskipTest
ENV PORT 8080

FROM openjdk:17 as runtime

WORKDIR /app
COPY --from=maven /app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
