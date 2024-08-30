FROM openjdk:17-alpine

ARG JAR=/target/*SNAPSHOT.jar

COPY ${JAR} auth.jar

EXPOSE 8083

ENTRYPOINT [ "java","-jar","auth.jar" ]
