FROM ubuntu:20.04

# install java
RUN apt-get update && apt-get install -y  openjdk-11-jdk

WORKDIR /book-shop-web/backend

ADD . /book-shop-web/backend

ENTRYPOINT [ "bash", "java-backend.sh" ]

EXPOSE 10000