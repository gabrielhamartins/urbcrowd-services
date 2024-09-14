FROM openjdk:21

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} urbcrowd.jar

EXPOSE 8080

CMD ["java", "-jar", "urbcrowd.jar"]