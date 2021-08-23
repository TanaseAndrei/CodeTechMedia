FROM openjdk:8
COPY target/CodeTechMedia-0.0.1-SNAPSHOT.jar /
ENTRYPOINT ["java", "-jar", "CodeTechMedia-0.0.1-SNAPSHOT.jar"]
