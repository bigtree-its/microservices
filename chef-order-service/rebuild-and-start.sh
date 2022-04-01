#!/bin/bash
gradle clean build
docker build -t bigtree/chef-order-service:v1 .
docker push bigtree/chef-order-service:v1
# docker-compose up --remove-orphans