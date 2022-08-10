FROM openjdk:8-jre
VOLUME /tmp
ADD target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-Duser.timezone=GMT+08","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=prod"]