FROM maven:3.6.3-jdk-11-openj9
RUN apt-get update && apt-get install -y curl
RUN curl -sL https://deb.nodesource.com/setup_18.x | bash 
RUN apt-get install -y nodejs  && apt-get install -y grunt && apt-get install -y git
RUN node -v
RUN npm -v
RUN apt-get install wget
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN dpkg -i google-chrome-stable_current_amd64.deb; apt-get -fy install
