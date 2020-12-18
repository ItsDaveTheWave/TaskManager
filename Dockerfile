FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
CMD ["dir"]
ENTRYPOINT ["java","-jar","/app.jar"]