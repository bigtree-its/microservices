#!/bin/bash
gradle clean build
docker build -t openecomm/order-service:v1 .
docker push openecomm/order-service:v1
# docker-compose up --remove-orphans