FROM maven:3.8.2-openjdk-17-slim as builder
WORKDIR /usr/src/app
COPY ./src ./src
COPY *.xml ./
RUN mvn package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app

RUN apk add --no-cache dumb-init
COPY --from=builder /usr/src/app/target/auth-null.jar ./auth.jar
ENTRYPOINT ["dumb-init", "java", "-jar", "-Dmyserver.bindPort=8080", "./auth.jar"]