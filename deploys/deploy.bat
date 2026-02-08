@echo off

cd ..

echo [INFO] Iniciando build do projeto...
call mvn clean package -Dspring-boot.run.profile=hml -DskipTests
IF ERRORLEVEL 1 (
    color 0C
    echo [ERRO] Falha no build Maven
    pause
    exit /b 1
)

echo [INFO] Subindo containers e buildando nova imagem...
docker compose down
docker compose up -d --build

IF ERRORLEVEL 1 (
    color 0C
    echo [ERRO] Falha ao subir containers
    pause
    exit /b 1
)

echo [SUCCESS] Deploy finalizado com sucesso!
pause
