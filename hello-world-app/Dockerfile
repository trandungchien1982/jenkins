FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.21_9
MAINTAINER tdchien
RUN mkdir /opt/app
COPY "build/libs/hello-world-0.0.1-SNAPSHOT.jar" /opt/app/japp.jar
CMD ["java", "-jar", "/opt/app/japp.jar"]
