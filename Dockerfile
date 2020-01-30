FROM maven:3-jdk-8-openj9

RUN apt-get update && apt-get install -y curl
RUN curl -sL https://deb.nodesource.com/setup_13.x | bash 
RUN apt-get install -y nodejs  && apt-get install -y grunt && apt-get install -y git
RUN node -v
RUN npm -v
RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list'
RUN apt-get install -y google-chrome-stable
