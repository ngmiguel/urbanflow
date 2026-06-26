FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY shared shared
COPY services services

RUN mvn package -pl services/digital-twin-service -am -DskipTests -q

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system urbanflow \
    && useradd --system --gid urbanflow urbanflow
USER urbanflow

COPY --from=build /app/services/digital-twin-service/target/digital-twin-service-*.jar app.jar

EXPOSE 8091
ENV SPRING_PROFILES_ACTIVE=docker

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl -f http://localhost:8091/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
