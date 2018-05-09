FROM navikt/java:8
LABEL maintainer="Team Aasmund"

COPY target/pam-cv-indexer-*.jar app.jar
COPY target/kafkatrust.jks .
EXPOSE 8080
