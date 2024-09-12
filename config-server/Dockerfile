FROM openjdk:17-alpine

ARG JAR=target/*.jar

COPY ${JAR} config.jar

EXPOSE 8888

ENTRYPOINT [ "java","-jar","config.jar" ]
