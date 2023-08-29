#!/bin/bash

echo stopping containers...
docker stop $(docker ps -aq) > /dev/null

echo removing containers...
docker rm $(docker ps -aq) > /dev/null

echo removing client-microservice image...
docker rmi $(docker images client-microservice -q) > /dev/null

echo -----containers-----
docker ps -a

echo -----images---------
docker images