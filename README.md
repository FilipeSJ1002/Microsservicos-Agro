# Microsserviços Agro

A plataforma é um sistema distribuído construído com base na arquitetura de microsserviços. A estrutura base de cada um dos microsserviços foi gerada utilizando o **Spring Initializr**.

## Pré-requisitos

- Java 21 ou superior.
- Docker e Docker Compose instalados.
- IDE (Visual Studio Code recomendado).

## Passo a Passo para Inicialização

### 1. Clonagem e Configuração do Ambiente

1. Clone o repositório em sua máquina:
   ```bash
   git clone https://github.com/FilipeSJ1002/Microsservicos-Agro.git
   cd Microsservicos-Agro
   ```

2. Crie os arquivos de ambiente (`.env`) baseando-se nos arquivos `.env.example` fornecidos:
   - Crie o `.env` na raiz do projeto (configurações do Banco de Dados do Docker).
   - Crie o `.env` dentro de cada pasta de microsserviço (`feed-cost-service`, `feed-management-service` e `nutrition-analysis-service`).
   - Copie os atributos exatos dos arquivos `.env.example` para os respectivos `.env` criados e ajuste usuários, senhas ou URLs, caso necessário.

### 2. Inicializando os Serviços de Infraestrutura

Com o terminal aberto na raiz do projeto onde está localizado o arquivo `docker-compose.yml`, suba as imagens do PostgreSQL, MongoDB e RabbitMQ rodando o comando:

```bash
docker-compose up -d
```
Aguarde os contêineres inicializarem para prosseguir.

### 3. Build e Execução dos Microsserviços

**Opção A: Pelo VSCode (Recomendado)**
No Visual Studio Code, execute o atalho `Ctrl + Shift + B`. Todos os servidores (Cost, Management e Nutrition) subirão nativamente em abas terminais divididas.

**Opção B: Manualmente via CLI**
Abra as 3 pastas dos microsserviços em terminais separados e execute em cada uma delas o Maven Wrapper:
```bash
.\mvnw spring-boot:run
```

## Documentação Interativa (Swagger)

A documentação dos contratos das rotas pode ser lida e testada diretamente via Swagger acessando as seguintes URLs em seu navegador após a inicialização:

> **Aviso Importante:** As URLs abaixo assumem as portas padrão (`8081`, `8082`, `8083`). Caso você tenha modificado essas portas através dos seus arquivos `.env`, lembre-se de substituir o número da porta nos links abaixo pela numeração correspondente.

- Feed Cost Service: http://localhost:8081/swagger-ui/index.html
- Feed Management Service: http://localhost:8082/swagger-ui/index.html
- Nutrition Analysis Service: http://localhost:8083/swagger-ui/index.html

## Arquitetura e Resiliência

Este projeto implementa padrões de mercado para sistemas distribuídos:

* **Idempotência no Consumo de Mensagens:** O `nutrition-analysis-service` possui uma trava de segurança utilizando uma collection auxiliar (`processed_events`) no MongoDB. Se o RabbitMQ duplicar a entrega de uma mensagem devido a instabilidades na rede, o sistema reconhece o ID do evento e ignora o processamento duplicado.
* **Resiliência entre Serviços:** As chamadas síncronas feitas via `RestTemplate` estão blindadas. Se o serviço de custos estiver fora do ar ou retornar *Not Found* (404), a falha é interceptada de forma graciosa para evitar loops infinitos de reprocessamento (Dead Letters) no RabbitMQ.
* **Tratamento Global de Exceções (`@ControllerAdvice`):** Todos os microsserviços possuem interceptadores globais de erro. Consultas a recursos inexistentes ou envios de JSON mal formatados (ex: Enums inválidos) não quebram a aplicação nem vazam *Stack Traces*, retornando sempre um JSON amigável e padronizado (Status 400 ou 404).
* **Prevenção de Floating Point Math:** Os valores financeiros (como o custo por Kg) são salvos em formato numérico inteiro (`Long` em centavos) nos bancos de dados (PostgreSQL e MongoDB) para evitar problemas de arredondamento. O `Double` só é formatado na camada de apresentação (DTO/Serialization) no momento em que a API devolve o JSON.


## Endpoints e Testes no Insomnia

Abaixo estão os guias das rotas e como efetuar disparos HTTP.

### 1. Feed Management Service (Porta 8082)

Porta de entrada principal para registro da alimentação consumida.

**POST http://localhost:8082/feeds**
- Exemplo de Requisição (JSON Body):
```json
{
  "animalId": "123",
  "feedType": "MILHO",
  "quantity": 10.5
}
```

- Exemplo de Resposta (Status HTTP 201 Created):
```json
{
	"animalId": "123",
	"feedType": "MILHO",
	"id": 1,
	"quantity": 10.5,
	"recordDate": "2026-04-12T04:00:37.6193961"
}
```

**GET http://localhost:8082/feeds**
Lista todos os registros de alimentação cadastrados contidos na tabela SQL.

**GET http://localhost:8082/feeds/{animalId}**
Lista os registros de um animal específico (ex: `/feeds/123`).

### 2. Feed Cost Service (Porta 8081)

Retorna a tabela de custos de insumos por Kg.

**GET http://localhost:8081/cost/{feedType}**
Retorna o custo de um tipo de alimentação específica (ex: `/cost/MILHO`).
- Exemplo de Resposta:
```json
{
  "feedType": "MILHO",
  "costPerKg": 2.50
}
```
*(Nota: Valores financeiros na plataforma são armazenados no Banco de Dados em centavos para evitar problemas de arredondamento com ponto flutuante. No entanto, no retorno da API, eles são convertidos automaticamente para Reais para facilitar a visualização).*

### 3. Nutrition Analysis Service (Porta 8083)

Serviço puramente analítico consultivo para recuperar eventos processados de forma reativa pelo RabbitMQ no MongoDB.

**GET http://localhost:8083/analysis**
Retorna a auditoria e todo o histórico de análises já calculadas no sistema.

**GET http://localhost:8083/analysis/{animalId}**
Consulta os históricos calculados de um único animal.
- Exemplo de Resposta:
```json
[
	{
		"analysisDate": "2026-04-12T04:18:35.015",
		"animalId": "12345",
		"costPerKg": 2.50,
		"feedType": "MILHO",
		"id": "69db474b8088712033224840",
		"quantity": 10.5,
		"totalCost": 26.25
	}
]
```