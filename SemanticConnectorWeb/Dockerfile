FROM node:lts-alpine
WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY .env ./
COPY src ./src
COPY public ./public

EXPOSE 3000
CMD ["npm", "start"]


