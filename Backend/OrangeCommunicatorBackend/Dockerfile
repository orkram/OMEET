# Autorzy kodu źródłowego: Bartosz Panuś
# Kod został utworzony w ramach kursu Projekt Zespołowy
# na Politechnice Wrocławskiej

#
# Build stage
#
#FROM maven:3.6.0-jdk-11-slim AS build
#COPY ./src /home/app/src
#COPY ./pom.xml /home/app
#WORKDIR /home/app
#RUN mvn -DskipTests clean package

#
# Package stage
#
FROM openjdk:11-jdk-slim
VOLUME /tmp
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]