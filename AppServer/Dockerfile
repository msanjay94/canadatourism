FROM node:10

WORKDIR /Users/sanjaykumar/docker/

COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 8443

CMD [ "node", "app.js" ]
