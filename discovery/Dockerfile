FROM openjdk:17-alpine

ARG JAR=/target/*SNAPSHOT.jar

COPY ${JAR} discovery.jar

EXPOSE 8761

ENTRYPOINT [ "java","-jar","discovery.jar" ]
