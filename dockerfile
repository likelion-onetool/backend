FROM openjdk:17-jdk

COPY server/build/libs/*SNAPSHOT.jar onetool-server.jar

ENTRYPOINT ["java", "-jar", "/onetool-server.jar"]
