@echo off
echo Iniciando microsservicos BovExo...

start "Feed Cost Service" cmd /k "cd feed-cost-service && .\mvnw spring-boot:run"

start "Feed Management Service" cmd /k "cd feed-management-service && .\mvnw spring-boot:run"

start "Nutrition Analysis Service" cmd /k "cd nutrition-analysis-service && .\mvnw spring-boot:run"

echo Servicos inicializados em janelas separadas.
