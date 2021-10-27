FROM maven:3.8.3-openjdk-8-slim as builder

WORKDIR /src
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src /src/src
RUN mvn package

FROM tomcat:8.5.72-jdk8-openjdk-slim-bullseye

COPY build/config.xml /etc/v1-plugin-backend.xml
ENV SCMBACKEND_CONFIG /etc/v1-plugin-backend.xml

VOLUME /var/lib/nexus

VOLUME /var/lib/plugin-backend
ENV SCMBACKEND_HOME /var/lib/plugin-backend

COPY build/redirect /usr/local/tomcat/webapps/ROOT
COPY --from=builder /src/target/scm-plugin-backend.war /usr/local/tomcat/webapps/scm-plugin-backend.war
