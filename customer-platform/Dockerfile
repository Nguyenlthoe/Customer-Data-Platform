FROM maven:3.6.0-jdk-8-slim AS build
WORKDIR /temp
ADD ./pom.xml /temp
RUN mvn verify clean --fail-never
ADD ./ /temp
RUN mvn package

FROM openjdk:8-jdk
ENV TIMEZONE "Asia/Ho_Chi_Minh"
ENV TZ=Asia/Ho_Chi_Minh

ENV HOME=/customer-platform
WORKDIR $HOME
EXPOSE 8280

COPY --from=build /temp/target/ .
CMD java -jar customer-platform-2.2.7.RELEASE.jar

