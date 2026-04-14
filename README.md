# Microsserviços Agro

A plataforma é um sistema distribuído construído com base na arquitetura de microsserviços, acompanhado de um Dashboard web interativo. A estrutura base de cada um dos microsserviços foi gerada utilizando o **Spring Initializr** e refatorada para aderir aos princípios **SOLID, Clean Architecture e CQRS**.

## Pré-requisitos

* Java 21 ou superior.
* Docker e Docker Compose instalados.
* Node.js (v18 ou superior) e npm (para o Front-end).
* IDE (Visual Studio Code recomendado).

## Passo a Passo para Inicialização

### 1. Clonagem e Configuração do Ambiente

1. Clone o repositório em sua máquina:

```bash
git clone https://github.com/FilipeSJ1002/Microsservicos-Agro.git
cd Microsservicos-Agro
```

2. Crie os arquivos de ambiente (`.env`) baseando-se nos arquivos `.env.example` fornecidos:

   * Crie o `.env` na raiz do projeto (configurações do Banco de Dados do Docker).
   * Crie o `.env` dentro de cada pasta de microsserviço (`authenticator-service`, `feed-cost-service`, `feed-management-service` e `nutrition-analysis-service`).
   * A injeção automática é gerenciada pelo **spring-dotenv**.
   * Copie os atributos exatos dos arquivos `.env.example` para os respectivos `.env` criados e ajuste usuários, senhas ou URLs, caso necessário.

---

### 2. Inicializando os Serviços de Infraestrutura

Com o terminal aberto na raiz do projeto onde está localizado o arquivo `docker-compose.yml`, suba as imagens do PostgreSQL, MongoDB e RabbitMQ rodando o comando:

```bash
docker-compose up -d
```

Aguarde os contêineres inicializarem para prosseguir.

---

### 3. Build e Execução dos Microsserviços

**Opção A: Pelo VSCode (Recomendado)**
No Visual Studio Code, execute o atalho `Ctrl + Shift + B`. Todos os **4 servidores (Auth, Cost, Management e Nutrition)** subirão nativamente em abas terminais divididas.

**Opção B: Manualmente via CLI**
Abra as 4 pastas dos microsserviços em terminais separados e execute em cada uma delas o Maven Wrapper:

```bash
./mvnw spring-boot:run
```

---

### 4. Inicializando o Painel Web (Front-end React)

Para facilitar a visualização e os testes da arquitetura, o projeto conta com um Dashboard interativo integrado via **Axios Interceptors**.

1. Abra um novo terminal na pasta `frontend-bovexo`.
2. Instale as dependências:

```bash
npm install
```

3. Inicie o servidor de desenvolvimento:

```bash
npm run dev
```

4. Acesse `http://localhost:5173` no seu navegador. Você poderá criar uma conta, fazer login ou entrar como Visitante.

---

## Documentação Interativa (Swagger)

A documentação dos contratos das rotas podem ser lidas e testadas diretamente via Swagger acessando as 	seguintes URLs:

> **Aviso Importante:** As URLs abaixo assumem as portas padrão (`8081`, `8082`, `8083`, `8084`). Caso você tenha modificado essas portas através dos seus arquivos `.env`, substitua conforme necessário.

* Authenticator Service: [http://localhost:8084/swagger-ui/index.html](http://localhost:8084/swagger-ui/index.html)
* Feed Cost Service: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
* Feed Management Service: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
* Nutrition Analysis Service: [http://localhost:8083/swagger-ui/index.html](http://localhost:8083/swagger-ui/index.html)

---

## Arquitetura e Resiliência

Este projeto implementa padrões de mercado para sistemas distribuídos de alta performance:

* **Segurança Centralizada e Stateless (JWT):**
  O `authenticator-service` emite os tokens JWT, enquanto os demais serviços utilizam um `JwtFilter` para validar as assinaturas localmente. A aplicação cria usuários padrão (**Admin, Guest e Service Accounts**) durante a inicialização.

* **Comunicação Service-to-Service:**
  Microsserviços autenticam-se entre si via `RestTemplate`, gerando tokens JWT temporários como **Service Accounts**, garantindo segurança ponta a ponta.

* **CQRS (Command Query Responsibility Segregation):**
  O `feed-management-service` separa operações de escrita (**Commands/POST**) das operações de leitura (**Queries/GET**), respeitando o princípio ISP.

* **Idempotência no Consumo de Mensagens:**
  O `nutrition-analysis-service` utiliza a collection `processed_events` no MongoDB para evitar reprocessamento de mensagens duplicadas do RabbitMQ.

* **Tratamento Global de Exceções (`@ControllerAdvice`):**
  Todos os microsserviços retornam respostas padronizadas em JSON sem expor stack traces.

* **Prevenção de Floating Point Math:**
  Valores financeiros são armazenados como `Long` (centavos) e convertidos para `Double` apenas na camada de apresentação (DTO).

---

## Testando a API Rapidamente (Insomnia / Postman)

### Como importar:

1. Abra o Insomnia.
2. Clique em `Create` → `Import from File`.
3. Selecione o arquivo `bovexo-insomnia-collection.yaml` na pasta `docs`.

---

### Como testar rotas protegidas manualmente

Todas as rotas exigem **JWT**.

1. Faça um POST para:

```
http://localhost:8084/auth/login
```

Body:

```json
{
  "username": "guest",
  "password": "guest123"
}
```

2. Copie o `accessToken` retornado.

3. Nas requisições protegidas:

   * Vá em **Auth**
   * Selecione **Bearer Token**
   * Cole o token

---

## Guia de Endpoints Essenciais

### 1. Authenticator Service (Porta 8084)

* **POST `/auth/register`**: Cadastra novo usuário.
* **POST `/auth/login`**: Retorna token JWT.

---

### 2. Feed Management Service (Porta 8082)

Porta principal de entrada.

* **POST `/feeds`** *(Requer Token)*
  Salva registro e publica evento no RabbitMQ.

* **GET `/feeds`** *(Requer Token)*
  Lista registros de alimentação.

---

### 3. Feed Cost Service (Porta 8081)

* **GET `/cost/{feedType}`** *(Requer Token)*
  Retorna custo por tipo de alimentação.

---

### 4. Nutrition Analysis Service (Porta 8083)

* **GET `/analysis`** *(Requer Token)*
  Retorna histórico completo.

* **GET `/analysis/{animalId}`** *(Requer Token)*
  Retorna histórico por animal.

---

## Acessando os Bancos de Dados Localmente

### 1. PostgreSQL (Usuários, Históricos e Custos)

Recomendo o uso do **DBeaver**.

**Configuração:**

* Host: `localhost`
* Porta: `5433`
* Database / Usuário / Senha: valores do `.env`

**Bancos:**

* `bovexo_auth` → usuários
* `bovexo_management` → eventos
* `bovexo_cost` → preços

---

### 2. MongoDB (Análises e Idempotência)

Recomendo o uso do **MongoDB Compass**.

**URI:**

```text
mongodb://localhost:27017/
```

**Banco:**

```
bovexo_nutrition
```

**Coleções:**

* `nutrition_analysis` → resultados finais
* `processed_events` → controle de idempotência