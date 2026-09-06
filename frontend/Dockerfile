# syntax=docker/dockerfile:1

# ---- Build stage: produce the Angular production bundle ----
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration production

# ---- Runtime stage: nginx serves static files + proxies the API ----
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY proxy_common.conf /etc/nginx/proxy_common.conf
COPY --from=build /app/dist/front-app /usr/share/nginx/html
EXPOSE 80
