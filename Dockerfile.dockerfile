FROM java:8
VOLUME /tmp
ADD /target/docker-spring-mongo.jar docker-spring-mongo.jar
EXPOSE 5000
RUN bash -c 'touch /docker-spring-mongo.jar'
ENTRYPOINT  ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","/docker-spring-mongo.jar"]
