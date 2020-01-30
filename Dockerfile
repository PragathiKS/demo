FROM maven:3-jdk-8-openj9

RUN apt-get update && apt-get install -y nodejs
RUN apt-get update && apt-get install -y npm
RUN apt-get update && apt-get install -y grunt && apt-get install -y git
RUN npm i npm@latest
RUN node -v
RUN npm -v
