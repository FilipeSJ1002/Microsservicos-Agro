Write-Host "Inicializando microsservicos BovExo..." -ForegroundColor Green

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd feed-cost-service; .\mvnw spring-boot:run" -WindowStyle Normal
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd feed-management-service; .\mvnw spring-boot:run" -WindowStyle Normal
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd nutrition-analysis-service; .\mvnw spring-boot:run" -WindowStyle Normal

Write-Host "Os servicos foram inicializados em janelas secundarias." -ForegroundColor Cyan
