FROM azul/zulu-openjdk:17 as fineract

WORKDIR /app
ENV REDIS_SERVER=redis://localhost:6379
COPY . .
CMD ["./gradlew","bootJar"]
ENTRYPOINT ["java", "-jar", "build/libs/interview-0.0.1-SNAPSHOT.jar"]