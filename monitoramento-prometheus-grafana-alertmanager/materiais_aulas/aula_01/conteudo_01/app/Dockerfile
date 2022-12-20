FROM openjdk:8-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring
RUN apk update
RUN apk upgrade
RUN apk --no-cache add curl
RUN apk add busybox-extras
USER spring:spring

ARG JAR_FILE=target/*jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Xms128M","-Xmx128M","-XX:PermSize=64m","-XX:MaxPermSize=128m","-Dspring.profiles.active=prod","-jar","/app.jar"]
