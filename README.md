# Urbcrowd Services

## Propósito

Backend do Urbcrowd, um aplicativo para população registrar reclamações com o intuito de dar mais visibilidade aos problemas da cidade que reside.

## Tecnologias

Java, Maven, Spring Boot, Docker, Git

## Setup

docker

OU

Java latest, Maven latest, Mongodb latest

## Building and deploying the application

### Build & run the application with docker

```bash
docker compose up --build
```

### Building the application without docker

```bash
mvn clean install
```

### Running the application (Requires mongodb running)

```bash
java -jar urbcrowd-services-x.x.x.jar
```