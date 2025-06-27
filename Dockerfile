FROM gradle:8.5.0-jdk21 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradlew ./gradlew
COPY gradle ./gradle

RUN gradle dependencies

COPY . .

RUN gradle build -x test

FROM amazoncorretto:21-al2023

WORKDIR /app

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar ./app.jar
COPY --from=builder /app/application.yml ./

EXPOSE 8080

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]