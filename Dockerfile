FROM openjdk:8-jdk-alpine
MAINTAINER satish.srivastava
ARG JAR_FILE=target/MyPingTest.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]	