FROM maven:3.8.3-openjdk-17

ADD /target/nir-0.0.1-SNAPSHOT.jar backend.jar

CMD ["java", "-jar", "backend.jar"]

## 1. Add pom.xml only here
#ADD ./pom.xml ./pom.xml
#
## 2. Start downloading dependencies
#RUN ["mvn", "verify", "clean", "--fail-never"]
#
## 3. Add all source code and start compiling
#ADD ./src ./src
#
#RUN ["mvn", "package"]
#
#EXPOSE 8080
#
#CMD ["java", "-jar", "./target/nir-0.0.1-SNAPSHOT.jar"]