FROM gradle:jdk AS builder
WORKDIR /app
COPY . .
RUN gradle clean test jacocoTestReport --no-daemon
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:22-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]