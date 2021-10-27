FROM maven:3.8.3-openjdk-8-slim as builder

WORKDIR /src
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src /src/src
RUN mvn package

FROM tomcat:8.5.72-jdk8-openjdk-slim-bullseye

COPY config/config.xml /etc/v1-plugin-backend.xml
ENV SCMBACKEND_CONFIG /etc/v1-plugin-backend.xml

VOLUME /var/lib/v1-plugin-backend
ENV SCMBACKEND_HOME /var/lib/v1-plugin-backend

COPY --from=builder /src/target/scm-plugin-backend.war /usr/local/tomcat/webapps/scm-plugin-backend.war

VOLUME /var/lib/nexus

