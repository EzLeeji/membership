FROM java:8
LABEL maintainer="LeejiEasy"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/pay-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} mbs.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mbs.jar"]