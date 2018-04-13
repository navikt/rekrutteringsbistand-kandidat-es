FROM navikt/java:8
LABEL maintainer="Team Aasmund"

COPY target/pam-cv-indexer-*.jar app.jar

EXPOSE 8080
