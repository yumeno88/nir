FROM maven:3.8.3-openjdk-17

ADD /target/nir-0.0.1-SNAPSHOT.jar backend.jar

CMD ["java", "-jar", "backend.jar"]