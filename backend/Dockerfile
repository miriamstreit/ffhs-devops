FROM maven:3.8.3-openjdk-17 AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine
WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/*.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
