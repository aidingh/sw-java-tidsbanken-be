FROM openjdk:17
ENV PORT 8080
ADD /target/Time-Bank-API-Project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]