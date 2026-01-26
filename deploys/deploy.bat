@echo off

cd ..

echo Iniciando build do projeto...

call mvn clean package -DskipTests
IF ERRORLEVEL 1 exit /b 1

echo Buildando imagem Docker...
docker build -t payments-service:latest .
IF ERRORLEVEL 1 exit /b 1

echo Derrubando containers...
docker compose down

echo Subindo containers...
docker compose up -d

echo Deploy finalizado com sucesso!
pause
