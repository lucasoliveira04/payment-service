@echo off

cd..

echo Derrubando containers...
docker compose down

echo Subindo containers...
docker compose up -d

echo Containers reiniciados com sucesso!
pause