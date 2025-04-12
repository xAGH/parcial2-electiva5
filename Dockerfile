FROM gradle:8.5-jdk-22 AS builder
WORKDIR /app
COPY build.gradle settings.gradle gradle.properties ./ 
COPY gradle ./gradle
RUN gradle build --no-daemon || return 0
RUN gradle clean test jacocoTestReport --no-daemon
RUN gradle build --no-daemon

FROM eclipse-temurin:22-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]
