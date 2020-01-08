FROM openjdk:8-jre-alpine
VOLUME /tmp
MAINTAINER leus
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
RUN sh -c 'touch /app.jar'
#对外端口
EXPOSE 8080
ENTRYPOINT ["java","-server","-jar","-Denv=dev","/app.jar"]