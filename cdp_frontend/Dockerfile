FROM node

# install
RUN apt update

WORKDIR /cdp_frontend/

ADD . /cdp_frontend/

RUN npm i

ENTRYPOINT [ "npm", "start" ]

# export port
EXPOSE 3100