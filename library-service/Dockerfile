FROM openjdk:17-alpine

ARG JAR=target/*.jar

COPY ${JAR} library.jar

EXPOSE 8081

ENTRYPOINT [ "java","-jar","library.jar" ]
