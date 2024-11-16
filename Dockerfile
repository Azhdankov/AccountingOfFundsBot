FROM gradle:8.10.2-jdk21-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:21-slim
EXPOSE 8080
RUN ls -a
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]