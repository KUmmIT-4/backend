FROM gradle:8.5.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle build

FROM amazoncorretto:21-alpine-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar ./app.jar
COPY --from=builder /app/application.yml ./
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]