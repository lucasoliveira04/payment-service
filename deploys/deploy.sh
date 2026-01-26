#!/bin/bash

cd ..

echo "Buildando projeto..."
mvn clean package -DskipTests || exit 1

echo "Buildando imagem Docker..."
docker build -t payments-service:latest . || exit 1

echo "Derrubando containers..."
docker compose down

echo "Subindo containers..."
docker compose up -d

echo "Deploy finalizado!"
