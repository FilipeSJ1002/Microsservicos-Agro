# Microsserviços Agro

Este projeto é um sistema de gestão de nutrição animal baseado em microsserviços, desenvolvido com Java 17 e Spring Boot.

## Arquitetura do Sistema

O sistema é composto por 3 microsserviços independentes que se comunicam via REST e mensageria (RabbitMQ):

1. **feed-cost-service (Porta 8081):** Fornece o catálogo de preços das rações. Utiliza PostgreSQL.
2. **feed-management-service (Porta 8082):** Registra o consumo dos animais e publica eventos na fila. Utiliza PostgreSQL.
3. **nutrition-analysis-service (Porta 8083):** Consome a fila, consulta o serviço de custos, calcula o total e salva o relatório. Utiliza MongoDB.

## Pré-requisitos

* Java 17
* Maven
* Docker (para rodar a infraestrutura de bancos e mensageria)

## 1. Subindo a Infraestrutura (Docker)

Execute os comandos abaixo no terminal para iniciar os containers necessários. 
*Nota: O PostgreSQL foi configurado na porta 5433 para evitar conflitos com instalações locais.*

PostgreSQL:  
docker run --name postgres-agro -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=admin -p 5433:5432 -d postgres

RabbitMQ:  
docker run -d --hostname my-rabbit --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

MongoDB:  
docker run -d --name mongo-agro -p 27017:27017 mongo

## 2. Configuração do Banco de Dados Relacional

Conecte-se ao PostgreSQL (localhost:5433 | user: postgres | pass: admin) e execute os seguintes passos:

1. Crie o banco de dados: bovexo_management
2. Crie o banco de dados: bovexo_cost
3. Conecte-se ao banco bovexo_cost e execute o script SQL abaixo para popular a tabela de preços:  
```
CREATE TABLE feed_cost (
    id SERIAL PRIMARY KEY,
    feed_type VARCHAR(50) NOT NULL UNIQUE,
    cost_per_kg DECIMAL(10,2) NOT NULL,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('MILHO', 2.50);
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('SOJA', 3.80);
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('FARELO_SOJA', 4.20);
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('SORGO', 2.10);
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('TRIGO', 2.90);
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('SUPLEMENTO_MINERAL', 5.50);
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('NUCLEO_PROTEICO', 6.75);
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('SAL_BRANCO', 1.20);
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('UREIA', 3.30);
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('SILAGEM_MILHO', 1.80);
```

## 3. Executando os Microsserviços

Abra os projetos na sua IDE de preferência e inicie os três serviços simultaneamente, pois eles dependem uns dos outros para o fluxo completo.

A ordem recomendada de inicialização é:
1. feed-cost-service
2. feed-management-service
3. nutrition-analysis-service

## 4. Como Testar o Fluxo Completo

Utilize o Postman, Insomnia ou curl para realizar as requisições.

Passo 1: Registrar o consumo (Isso salvará no Postgres e enviará para o RabbitMQ)

POST http://localhost:8082/feeds  

Header:
```  
Content-Type: application/json  
```
Body:
```
{
  "animalId": "123",
  "feedType": "MILHO",
  "quantity": 10
}
```

Passo 2: Consultar a análise gerada (O relatório completo salvo no MongoDB com o custo total calculado)  

GET http://localhost:8083/analysis/123