FROM openjdk:8-jre-alpine
VOLUME /tmp
MAINTAINER leus
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
RUN apk add dmidecode
RUN sh -c 'touch /app.jar'
LABEL SERVICE_TYPE=配置中心服务
LABEL OS=X86_64
#对外端口
EXPOSE 8080
ENTRYPOINT ["java","-server","-jar","-XX:MetaspaceSize=128m","-XX:MaxMetaspaceSize=256m","-Xms512m","-Xmx4096m","-Xmn32m","-Xss256k","-XX:SurvivorRatio=8","-XX:+UseConcMarkSweepGC","/app.jar","--spring.profiles.active=prod"]