# Microsserviços Agro

A plataforma é um sistema distribuído construído com base na arquitetura de microsserviços, acompanhado de um Dashboard web interativo. A estrutura base de cada um dos microsserviços foi gerada utilizando o **Spring Initializr**.

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
   * Crie o `.env` dentro de cada pasta de microsserviço (`feed-cost-service`, `feed-management-service` e `nutrition-analysis-service`).
   * Copie os atributos exatos dos arquivos `.env.example` para os respectivos `.env` criados e ajuste usuários, senhas ou URLs, caso necessário.

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
./mvnw spring-boot:run
```

### 4. Inicializando o Painel Web (Front-end React)

Para facilitar a visualização e os testes da arquitetura, o projeto conta com um Dashboard interativo.

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

## Documentação Interativa (Swagger)

A documentação dos contratos das rotas pode ser lida e testada diretamente via Swagger acessando as seguintes URLs em seu navegador após a inicialização:

> **Aviso Importante:** As URLs abaixo assumem as portas padrão (`8081`, `8082`, `8083`). Caso você tenha modificado essas portas através dos seus arquivos `.env`, lembre-se de substituir o número da porta nos links abaixo pela numeração correspondente.

* Feed Cost Service: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
* Feed Management Service: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
* Nutrition Analysis Service: [http://localhost:8083/swagger-ui/index.html](http://localhost:8083/swagger-ui/index.html)

## Arquitetura e Resiliência

Este projeto implementa padrões de mercado para sistemas distribuídos:

* **Segurança de Borda (Edge Security):** O `feed-management-service` atua como porta de entrada e é protegido por Spring Security com **JWT Stateless**. A aplicação cria automaticamente um usuário visitante no banco durante a inicialização (Data Seeding) para facilitar testes de integração.

* **Idempotência no Consumo de Mensagens:** O `nutrition-analysis-service` possui uma trava de segurança utilizando uma collection auxiliar (`processed_events`) no MongoDB. Se o RabbitMQ duplicar a entrega de uma mensagem devido a instabilidades na rede, o sistema reconhece o ID do evento e ignora o processamento duplicado.

* **Resiliência entre Serviços:** As chamadas síncronas feitas via `RestTemplate` estão blindadas. Se o serviço de custos estiver fora do ar ou retornar *Not Found* (404), a falha é interceptada de forma graciosa para evitar loops infinitos de reprocessamento (Dead Letters) no RabbitMQ.

* **Tratamento Global de Exceções (`@ControllerAdvice`):** Todos os microsserviços possuem interceptadores globais de erro. Consultas a recursos inexistentes ou envios de JSON mal formatados não quebram a aplicação nem vazam *Stack Traces*, retornando sempre um JSON amigável e padronizado.

* **Prevenção de Floating Point Math:** Os valores financeiros (como o custo por Kg) são salvos em formato numérico inteiro (`Long` em centavos) nos bancos de dados para evitar problemas de arredondamento. O `Double` só é formatado na camada de apresentação (DTO) no momento em que a API devolve o JSON.

---

## Testando a API Rapidamente (Insomnia / Postman)

Para facilitar a avaliação das rotas sem precisar digitar URLs ou montar corpos JSON manualmente, disponibilizei uma **Collection do Insomnia** pré-configurada.

### Como importar:

1. Abra o Insomnia.
2. Clique em `Create` no canto superior direito e selecione `Import from File`.
3. Escolha o arquivo `bovexo-insomnia-collection.yaml` localizado na pasta `docs` deste repositório.

### Como testar as rotas protegidas manualmente

A rota de registro de alimentação (`POST /feeds`) está protegida pelo Spring Security. Para testá-la no Insomnia/Postman sem criar um usuário, utilize a **Rota de Visitante**:

1. Utilize o HTTP request já criado chamado Login na collection do Insomnia pré-configurada que foi disponibilizada. Ele realiza automaticamente um POST para `http://localhost:8082/auth/guest`, retornando um JSON com um token de acesso.
2. Copie o token retornado.
3. Na requisição `POST /feeds`, vá na aba de Autenticação (Auth), selecione **Bearer Token**, cole o token e envie.

### Guia de Endpoints Essenciais

#### 1. Autenticação (Porta 8082)

* **POST `/auth/register`**: Cadastra um novo usuário (Requer `username` e `password` no JSON).
* **POST `/auth/login`**: Realiza o login e devolve o token JWT.
* **POST `/auth/guest`**: Gera um token JWT instantâneo de visitante (Sem necessidade de credenciais).

#### 2. Feed Management Service (Porta 8082)

Porta de entrada principal para registro da alimentação consumida.

**POST `/feeds`** *(Requer Token Bearer)*

Exemplo de Requisição (JSON Body):

```json
{
  "animalId": "123",
  "feedType": "MILHO",
  "quantity": 10.5
}
```

**GET `/feeds`** *(Público)*
Lista todos os registros de alimentação cadastrados contidos na tabela SQL.

#### 3. Feed Cost Service (Porta 8081)

Retorna a tabela de custos de insumos por Kg.

**GET `/cost/{feedType}`** *(Público)*
Retorna o custo de um tipo de alimentação específica (ex: `/cost/MILHO`).

#### 4. Nutrition Analysis Service (Porta 8083)

Serviço puramente analítico consultivo para recuperar eventos processados de forma reativa pelo RabbitMQ no MongoDB.

**GET `/analysis`** *(Público)*
Retorna a auditoria e todo o histórico de análises já calculadas no sistema.


**GET `/analysis/{animalId}`** *(Público)*
Consulta os históricos calculados de um único animal.
Exemplo de Resposta:
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