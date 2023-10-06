FROM openjdk:latest

ARG SERVER_PORT

EXPOSE ${SERVER_PORT}

COPY target/*.jar /events-api/app.jar

VOLUME /events-api

WORKDIR /events-api

CMD java -jar app.jar

HEALTHCHECK \
--interval=30s \
--timeout=5s \
--start-period=120s \
--retries=3 \
CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1