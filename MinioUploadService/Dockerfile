FROM mhart/alpine-node

RUN mkdir -p /var/www/app
COPY minioUpload /var/www/app

WORKDIR /var/www/app

RUN npm install

EXPOSE 9010

CMD ["node","server.js"]