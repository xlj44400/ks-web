FROM nginx:alpine

COPY deployment/nginx/sites/localhost.conf /etc/nginx/conf.d/default.conf
COPY deployment/nginx/nginx.conf /etc/nginx/nginx.conf

COPY build /usr/share/nginx/html
