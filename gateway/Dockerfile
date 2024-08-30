FROM openjdk:17-alpine

ARG JAR=/target/*SNAPSHOT.jar

COPY ${JAR} gateway.jar

EXPOSE 8222

ENTRYPOINT [ "java","-jar","gateway.jar" ]
