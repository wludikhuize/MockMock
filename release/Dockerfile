# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine
# copy JAR into image
COPY /MockMock.jar /MockMock.jar
# run application with this command line 
CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=default", "/MockMock.jar", "-p 25", "-h 80", "-w 8080"]