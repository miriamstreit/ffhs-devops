FROM node:16 AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY ./ .
RUN npm run build

FROM nginx AS production-stage
RUN mkdir /app
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY docker/conf/nginx.conf /etc/nginx/conf.d/default.conf
COPY docker/generate-env-file.sh /docker-entrypoint.d/
RUN chmod +x /docker-entrypoint.d/generate-env-file.sh
