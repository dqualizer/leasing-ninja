FROM maven:3-eclipse-temurin-21-alpine as builder

WORKDIR /usr/src/app

COPY pom.xml .

COPY leasingninja-sales ./leasingninja-sales
COPY leasingninja-riskmanagement ./leasingninja-riskmanagement
COPY leasingninja-webapp ./leasingninja-webapp

RUN mvn install -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine as rt

WORKDIR /usr/src/app

COPY --from=builder /usr/src/app/leasingninja-webapp/target/leasingninja-webapp-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD [ "java", "-jar", "leasingninja-webapp-0.0.1-SNAPSHOT.jar", "-Dspring-boot.run.jvmArguments=-enableassertions", "-Dspring-boot.run.arguments=--logging.level.io.leasingninja=TRACE" ]
