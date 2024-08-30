FROM openjdk:17-alpine

ARG JAR=/target/*SNAPSHOT.jar

COPY ${JAR} config.jar

EXPOSE 8888

ENTRYPOINT [ "java","-jar","config.jar" ]
