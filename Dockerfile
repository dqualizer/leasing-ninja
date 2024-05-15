FROM maven:3-eclipse-temurin-21-alpine as builder

WORKDIR /usr/src/app

COPY pom.xml .

COPY leasingninja-sales ./leasingninja-sales
COPY leasingninja-riskmanagement ./leasingninja-riskmanagement
COPY leasingninja-webapp ./leasingninja-webapp
COPY leasingninja-riskApi ./leasingninja-riskApi

RUN mvn install -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine as rt

WORKDIR /usr/src/app

COPY --from=builder /usr/src/app/leasingninja-webapp/target/leasingninja-webapp-0.0.1-SNAPSHOT.jar .
COPY --from=builder /usr/src/app/leasingninja-riskApi/target/leasingninja-riskApi-0.0.1-SNAPSHOT.jar .

EXPOSE 7080 7081

CMD [ "java", "-jar", "leasingninja-webapp-0.0.1-SNAPSHOT.jar", "-Dspring-boot.run.jvmArguments=-enableassertions", "-Dspring-boot.run.arguments=--logging.level.io.leasingninja=TRACE" ]
CMD [ "java", "-jar", "leasingninja-riskApi-0.0.1-SNAPSHOT.jar", "-Dspring-boot.run.jvmArguments=-enableassertions", "-Dspring-boot.run.arguments=--logging.level.io.leasingninja=TRACE" ]
