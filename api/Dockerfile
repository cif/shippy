FROM maven:latest
WORKDIR /app
COPY ./src /app/src
COPY ./pom.xml /app/pom.xml
RUN mvn clean install -DskipTests
ENTRYPOINT ["java","-jar","target/app-0.0.1-SNAPSHOT.jar"]