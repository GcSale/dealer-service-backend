FROM openjdk:14 as BUILD_IMAGE

WORKDIR /app
COPY dealer-backend/build.gradle.kts dealer-backend/settings.gradle.kts dealer-backend/gradlew /app/
COPY dealer-backend/gradle /app/gradle
RUN ./gradlew build; exit 0

COPY dealer-backend/src /app/src