FROM openjdk:17-alpine

ARG JAR=/target/*SNAPSHOT.jar

COPY ${JAR} book.jar

EXPOSE 8082

ENTRYPOINT [ "java","-jar","book.jar" ]
