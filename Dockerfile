FROM maven:latest AS build

WORKDIR /app
ADD . /app

RUN mvn package -DskipTests

FROM eclipse-temurin:latest

WORKDIR /app
COPY --from=build /app/target/*.jar urbcrowd.jar

EXPOSE 8080

CMD ["java", "-jar", "urbcrowd.jar"]