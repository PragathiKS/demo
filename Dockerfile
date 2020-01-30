FROM maven:3-jdk-8-openj9

RUN apt-get update && apt-get install -y nodejs
RUN apt-get update && apt-get install -y grunt

RUN npm install -g npm
RUN node -v
RUN npm -v