# Tech Events Registration

API REST para gestão de eventos técnicos, participantes e inscrições, desenvolvida em Java com Spring Boot.

## Visão geral

O Tech Events Registration é uma aplicação de cadastro e acompanhamento de eventos, com foco em:

- cadastro e consulta de eventos;
- cadastro e consulta de participantes;
- criação de inscrições em eventos;
- controle de vagas por evento;
- paginação de listagens;
- respostas padronizadas de erro;
- autenticação básica com Spring Security para ações administrativas.

A aplicação foi pensada como um projeto educacional para praticar arquitetura em camadas, persistência com JPA e boas práticas de API REST.

## Stack tecnológica

- Java 17
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Spring Security
- MySQL 8+
- H2 (presente na dependência para uso local/testes)
- Maven
- Lombok
- MapStruct
- Springdoc OpenAPI / Swagger UI
- JUnit 5

## Arquitetura

A aplicação segue uma arquitetura em camadas, com separação clara entre responsabilidades:

```text
┌─────────────────────────────┐
│        Controllers          │  Recebem e validam as requisições HTTP
├─────────────────────────────┤
│          Services           │  Contêm a lógica de negócio
├─────────────────────────────┤
│        Repositories        │  Acesso aos dados com Spring Data JPA
├─────────────────────────────┤
│       Entity / DTO          │  Modelos e transfer de dados
├─────────────────────────────┤
│          Mapper             │  Converte entidades para DTOs e vice-versa
├─────────────────────────────┤
│         MySQL / JPA         │  Persistência de eventos, participantes e inscrições
└─────────────────────────────┘
```

### Estrutura do projeto

```text
src/
├── main/
│   ├── java/com/jonas/TechEventsRegistration/
│   │   ├── Configs/
│   │   │   └── SecurityConfig.java
│   │   ├── Configurer/
│   │   │   └── MvcConfigurer/
│   │   │       └── MvcConfigurer.java
│   │   ├── Controllers/
│   │   │   ├── EventController.java
│   │   │   ├── ParticipantController.java
│   │   │   └── EnrollmentController.java
│   │   ├── DTO/
│   │   │   ├── Event/
│   │   │   ├── Participant/
│   │   │   └── Enrollment/
│   │   ├── Entity/
│   │   │   ├── Event.java
│   │   │   ├── Participant.java
│   │   │   └── Enrollment.java
│   │   ├── Exceptions/
│   │   ├── ExceptionsHandler/
│   │   ├── Mappers/
│   │   ├── Repository/
│   │   ├── Services/
│   │   └── TechEventsRegistrationApplication.java
│   └── resources/
│       └── application.yaml
├── test/
│   └── java/com/jonas/TechEventsRegistration/
│       └── TechEventsRegistrationApplicationTests.java
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Entidades e regras de negócio

### Event

Campos da entidade:

- id
- eventName
- description
- local
- category
- vacancies
- maxVacancies
- eventDateAndHours

Regra principal:

- `vacancies` não pode exceder `maxVacancies`.
- `vacancies` representa o número atual de vagas disponíveis.

### Participant

Campos:

- id
- participantName
- email
- phoneNumber
- institution

Validação aplicada:

- `email` deve ser válido pelo Bean Validation;
- campos obrigatórios são validados na camada de DTO.

### Enrollment

Relaciona um `Participant` com um `Event`:

- id
- participant
- event
- enrollmentDate

Regra principal:

- ao criar a inscrição, a quantidade de vagas do evento é decrementada;
- ao remover a inscrição, a vaga é devolvida;
- se a quantidade de vagas chegar a zero, a aplicação rejeita novas inscrições.

## Dependências e configuração

O projeto usa as dependências principais abaixo, definidas no `pom.xml`:

- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `spring-boot-starter-webmvc`
- `spring-boot-starter-validation`
- `mysql-connector-j`
- `h2`
- `spring-boot-h2console`
- `lombok`
- `mapstruct`
- `spring-boot-devtools`
- `spring-boot-starter-test`
- `springdoc-openapi-starter-webmvc-ui`

### Configuração de banco

O arquivo `src/main/resources/application.yaml` é o ponto de configuração principal:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tech_events?createDatabaseIfNotExist=true
    username: root
    password: jjnic@j0n
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

Antes de iniciar a aplicação, certifique-se de que o MySQL esteja ativo e que o banco `tech_events` exista ou possa ser criado automaticamente.

Se quiser testar com banco em memória em vez do MySQL, é possível ajustar a configuração para H2, mas o projeto foi estruturado para uso com MySQL como base principal.

## Como rodar a aplicação

### Pré-requisitos

- Java 17+
- Maven 3.8+
- MySQL 8+
- Git

### 1) Clonar o repositório

```bash
git clone https://github.com/seu-usuario/TechEventsRegistration.git
cd TechEventsRegistration
```

### 2) Configurar o banco

Crie o banco ou deixe o Spring criar automaticamente com `createDatabaseIfNotExist=true`:

```sql
CREATE DATABASE tech_events;
```

### 3) Instalar dependências

```bash
mvn clean install
```

### 4) Executar a aplicação

```bash
mvn spring-boot:run
```

A aplicação sobe em:

```text
http://localhost:8080
```

## Segurança e autenticação

A API usa Spring Security com autenticação HTTP Basic.

### Usuários em memória

No `SecurityConfig`, existem dois usuários cadastrados:

- `Jonathan Leão` / `JJnic@J0n` -> roles `USER`, `ADMIN`
- `Jonas` / `JJnic@J0n` -> role `USER`

### Regras

- endpoints públicos: consultas e leitura sem autenticação;
- endpoints com `/admin` exigem autenticação e perfil de administrador;
- ações de criação, atualização e exclusão em eventos, participantes e inscrições estão protegidas.

Exemplo de autenticação via curl:

```bash
curl -u 'Jonathan Leão:JJnic@J0n' http://localhost:8080/Events/admin?page=0\&size=6
```

No Postman, use a aba Authorization e selecione `Basic Auth`.

## Swagger / OpenAPI

Como o projeto inclui `springdoc-openapi-starter-webmvc-ui`, a documentação interativa fica em:

```text
http://localhost:8080/swagger-ui/index.html
```

A documentação em JSON fica em:

```text
http://localhost:8080/v3/api-docs
```

## Endpoints da API

A base da API é:

```text
http://localhost:8080
```

### Visão geral dos endpoints

| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| GET | `/Events/admin` | ADMIN | Lista paginada de eventos |
| GET | `/Events/{id}` | Pública | Busca evento por id |
| GET | `/Events/find` | Pública | Busca evento por nome |
| POST | `/Events/admin` | ADMIN | Cria evento |
| PUT | `/Events/admin/{id}` | ADMIN | Atualiza evento |
| DELETE | `/Events/admin/{id}` | ADMIN | Remove evento |
| GET | `/Participants/admin` | ADMIN | Lista paginada de participantes |
| GET | `/Participants/{id}` | Pública | Busca participante por id |
| GET | `/Participants/find` | Pública | Busca participante por nome |
| POST | `/Participants/admin` | ADMIN | Cria participante |
| PUT | `/Participants/admin/{id}` | ADMIN | Atualiza participante |
| DELETE | `/Participants/admin/{id}` | ADMIN | Remove participante |
| GET | `/Enrollments/admin` | ADMIN | Lista paginada de inscrições |
| GET | `/Enrollments/{id}` | Pública | Busca inscrição por id |
| POST | `/Enrollments/admin` | ADMIN | Cria inscrição |
| PUT | `/Enrollments/admin/{id}` | ADMIN | Atualiza inscrição |
| DELETE | `/Enrollments/admin/{id}` | ADMIN | Remove inscrição |

### 1) Eventos

#### Listar eventos paginados

```http
GET /Events/admin?page=0&size=6
Authorization: Basic <base64(username:password)>
```

Exemplo com curl:

```bash
curl -u 'Jonathan Leão:JJnic@J0n' 'http://localhost:8080/Events/admin?page=0&size=6'
```

Resposta esperada:

```json
{
  "content": [
    {
      "eventName": "Java na prática",
      "description": "Workshop sobre Java e Spring Boot",
      "local": "São Paulo",
      "category": "Backend",
      "eventDateAndHours": "2026-08-20T19:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 6,
    "sort": {
      "unsorted": true,
      "sorted": false,
      "empty": true
    }
  },
  "totalElements": 1
}
```

#### Buscar evento por ID

```http
GET /Events/1
```

Resposta:

```json
{
  "eventName": "Java na prática",
  "description": "Workshop sobre Java e Spring Boot",
  "local": "São Paulo",
  "category": "Backend",
  "eventDateAndHours": "2026-08-20T19:00:00"
}
```

#### Buscar eventos por nome

```http
GET /Events/find?name=Java
```

#### Criar evento

```http
POST /Events/admin
Content-Type: application/json
Authorization: Basic <base64(username:password)>
```

Exemplo de payload:

```json
{
  "eventName": "Java na prática",
  "description": "Workshop sobre Java e Spring Boot",
  "local": "São Paulo",
  "category": "Backend",
  "vacancies": 30,
  "maxVacancies": 30,
  "eventDateAndHours": "20/08/2026 19:00"
}
```

Resposta de sucesso (201 Created):

```json
{
  "eventName": "Java na prática",
  "description": "Workshop sobre Java e Spring Boot",
  "local": "São Paulo",
  "category": "Backend",
  "eventDateAndHours": "2026-08-20T19:00:00"
}
```

Observação:

- `vacancies` deve ser menor ou igual a `maxVacancies`.
- o formato da data e hora é `dd/MM/yyyy HH:mm`.

#### Atualizar evento

```http
PUT /Events/admin/1
Content-Type: application/json
Authorization: Basic <base64(username:password)>
```

Payload:

```json
{
  "eventName": "Java na prática - edição",
  "description": "Workshop avançado de Java e Spring Boot",
  "local": "Rio de Janeiro",
  "category": "Backend",
  "vacancies": 20,
  "maxVacancies": 30,
  "eventDateAndHours": "25/08/2026 18:30"
}
```

#### Excluir evento

```http
DELETE /Events/admin/1
Authorization: Basic <base64(username:password)>
```

Resposta:

```http
204 No Content
```

### 2) Participantes

#### Listar participantes paginados

```http
GET /Participants/admin?page=0&size=6
Authorization: Basic <base64(username:password)>
```

#### Buscar participante por ID

```http
GET /Participants/1
```

Resposta:

```json
{
  "participantName": "Jonas",
  "email": "jonas@email.com"
}
```

#### Buscar participantes por nome

```http
GET /Participants/find?name=Jonas
```

#### Criar participante

```http
POST /Participants/admin
Content-Type: application/json
Authorization: Basic <base64(username:password)>
```

Payload:

```json
{
  "participantName": "Maria Souza",
  "email": "maria@email.com",
  "phoneNumber": "11999999999",
  "institution": "Universidade Federal do Rio de Janeiro"
}
```

Resposta esperada (201 Created):

```json
{
  "participantName": "Maria Souza",
  "email": "maria@email.com"
}
```

#### Atualizar participante

```http
PUT /Participants/admin/1
Content-Type: application/json
Authorization: Basic <base64(username:password)>
```

Payload:

```json
{
  "participantName": "Maria Souza da Silva",
  "email": "maria.silva@email.com",
  "phoneNumber": "11888888888",
  "institution": "Universidade de São Paulo"
}
```

#### Excluir participante

```http
DELETE /Participants/admin/1
Authorization: Basic <base64(username:password)>
```

Resposta:

```http
204 No Content
```

### 3) Inscrições

#### Listar inscrições paginadas

```http
GET /Enrollments/admin?page=0&size=6
Authorization: Basic <base64(username:password)>
```

#### Buscar inscrição por ID

```http
GET /Enrollments/1
```

Resposta:

```json
{
  "id": 1,
  "participant": {
    "participantName": "Maria Souza",
    "email": "maria@email.com"
  },
  "event": {
    "eventName": "Java na prática",
    "description": "Workshop sobre Java e Spring Boot",
    "local": "São Paulo",
    "category": "Backend",
    "eventDateAndHours": "2026-08-20T19:00:00"
  }
}
```

#### Criar inscrição

```http
POST /Enrollments/admin
Content-Type: application/json
Authorization: Basic <base64(username:password)>
```

Payload:

```json
{
  "participantId": 1,
  "eventId": 1,
  "enrollmentDate": "12/08/2026"
}
```

Resposta esperada (201 Created):

```json
{
  "id": 1,
  "participant": {
    "participantName": "Maria Souza",
    "email": "maria@email.com"
  },
  "event": {
    "eventName": "Java na prática",
    "description": "Workshop sobre Java e Spring Boot",
    "local": "São Paulo",
    "category": "Backend",
    "eventDateAndHours": "2026-08-20T19:00:00"
  }
}
```

Regra importante:

- ao criar uma inscrição, a API decrementa 1 vaga do evento;
- se não houver vagas disponíveis, a aplicação responde com `400 Bad Request`.

#### Atualizar inscrição

```http
PUT /Enrollments/admin/1
Content-Type: application/json
Authorization: Basic <base64(username:password)>
```

Payload:

```json
{
  "participantId": 2,
  "eventId": 1,
  "enrollmentDate": "15/08/2026"
}
```

#### Excluir inscrição

```http
DELETE /Enrollments/admin/1
Authorization: Basic <base64(username:password)>
```

Resposta:

```http
204 No Content
```

## Códigos de resposta e erros

A API usa respostas padronizadas por `@RestControllerAdvice`.

### Códigos mais comuns

- `200 OK` - operação bem-sucedida de leitura/atualização
- `201 Created` - recurso criado com sucesso
- `204 No Content` - exclusão bem-sucedida
- `400 Bad Request` - dados inválidos, limite de vagas excedido ou sem vagas disponíveis
- `404 Not Found` - registro não encontrado
- `500 Internal Server Error` - erro inesperado do servidor

### Estrutura de erro

```json
{
  "title": "No Vacancies Available Exception",
  "status": 400,
  "timestamp": "2026-08-20T23:11:40",
  "message": "No vacancies dispo for this event"
}
```

## Testando a aplicação

### 1) Teste rápido com curl

#### Criar um evento

```bash
curl -u 'Jonathan Leão:JJnic@J0n' -X POST http://localhost:8080/Events/admin \
  -H 'Content-Type: application/json' \
  -d '{
    "eventName": "Java na prática",
    "description": "Workshop sobre Java e Spring Boot",
    "local": "São Paulo",
    "category": "Backend",
    "vacancies": 30,
    "maxVacancies": 30,
    "eventDateAndHours": "20/08/2026 19:00"
  }'
```

#### Criar um participante

```bash
curl -u 'Jonathan Leão:JJnic@J0n' -X POST http://localhost:8080/Participants/admin \
  -H 'Content-Type: application/json' \
  -d '{
    "participantName": "Maria Souza",
    "email": "maria@email.com",
    "phoneNumber": "11999999999",
    "institution": "USP"
  }'
```

#### Criar uma inscrição

```bash
curl -u 'Jonathan Leão:JJnic@J0n' -X POST http://localhost:8080/Enrollments/admin \
  -H 'Content-Type: application/json' \
  -d '{
    "participantId": 1,
    "eventId": 1,
    "enrollmentDate": "12/08/2026"
  }'
```

### 2) Teste via Swagger UI

1. inicie a aplicação;
2. abra `http://localhost:8080/swagger-ui/index.html`;
3. autentique com o usuário `Jonathan Leão` e senha `JJnic@J0n`;
4. teste os endpoints diretamente na interface.

### 3) Teste via Postman

Use as rotas listadas acima e configure autenticação básica nas requisições administrativas.



