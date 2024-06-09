#
# Build stage
#
FROM gradle:jdk17 AS build
COPY . .
RUN gradle clean build -x test

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /home/gradle/build/libs/*.jar demo.jar

# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]
