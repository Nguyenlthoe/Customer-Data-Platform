FROM node

# install
RUN apt update

WORKDIR /cdp_frontend/

ADD . /cdp_frontend/

RUN "cd cdp_frontend && npm i"

ENTRYPOINT [ "npm", "start" ]

# export port
EXPOSE 3100