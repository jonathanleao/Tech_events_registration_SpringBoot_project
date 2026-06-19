# 🎤 Tech Events Registration

> Um sistema completo de gerenciamento e inscrição de eventos técnicos, desenvolvido com **Spring Boot 4** e **Java 17**. Projeto educacional em desenvolvimento contínuo.

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen?style=flat-square)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=flat-square)](https://maven.apache.org/)
[![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow?style=flat-square)]()

---

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Stack Tecnológico](#stack-tecnológico)
- [Arquitetura](#arquitetura)
- [Instalação](#instalação)
- [Como Executar](#como-executar)
- [Endpoints da API](#endpoints-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Diagrama ER](#diagrama-er)
- [Status do Desenvolvimento](#status-do-desenvolvimento)
- [Próximas Melhorias](#próximas-melhorias)
- [Testando com Postman](#testando-com-postman)
- [Contribuindo](#contribuindo)

---

## 🎯 Visão Geral

O **Tech Events Registration** é uma API REST desenvolvida com fins educacionais para praticar conceitos de desenvolvimento web moderno. O sistema permite:

- ✅ Criar, ler, atualizar e deletar **eventos técnicos**
- ✅ Gerenciar **participantes** e seus dados
- ✅ Realizar **inscrições** em eventos
- ✅ Consultar informações com **paginação**
- ✅ Tratamento robusto de **erros e exceções**

### 📊 Estatísticas do Projeto

| Métrica | Valor |
|---------|-------|
| Entidades | 3 (Event, Participant, Enrollment) |
| Endpoints | 18 |
| Controllers | 3 |
| Services | 3 |
| Repositories | 3 |
| DTOs | 6 (3 Post + 3 Put) |
| Status | 🟡 Intermediário (em evolução) |

---

## 🛠️ Stack Tecnológico

### Backend
- **Framework:** Spring Boot 4.0.6
- **Linguagem:** Java 17
- **Build Tool:** Maven 3.8+

### Database
- **SGBD:** MySQL 8.0
- **ORM:** Spring Data JPA + Hibernate
- **H2:** Para testes (desenvolvimento)

### Dependências Principais
- **Lombok 1.18.38** - Redução de boilerplate
- **MapStruct 1.6.3** - Mapeamento de objetos
- **Jackson** - Serialização JSON
- **Spring Validation** - Validação de dados

### Testes
- **Framework:** JUnit 5 (Spring Boot Test)
- **Tool:** Postman (testes manuais de endpoints)

---

## 🏗️ Arquitetura

O projeto segue a arquitetura em **3 camadas**:

```
┌─────────────────────────────┐
│     Controller              │ ← REST Endpoints
├─────────────────────────────┤
│     Service                 │ ← Lógica de Negócio
├─────────────────────────────┤
│     Repository              │ ← Acesso a Dados
├─────────────────────────────┤
│     Database (MySQL)        │ ← Persistência
└─────────────────────────────┘
```

### Padrões Utilizados

| Padrão | Descrição | Localização |
|--------|-----------|-------------|
| **Repository** | Abstração de dados | `Repository/` |
| **Service** | Lógica de negócio | `Services/` |
| **DTO** | Transferência de dados | `DTO/` |
| **Mapper** | Conversão de objetos | `Mappers/` |
| **Exception Handler** | Tratamento centralizado de erros | `ExceptionsHandler/` |

---

## 📦 Instalação

### Pré-requisitos

- ✔️ Java 17+ instalado
- ✔️ Maven 3.8+ instalado
- ✔️ MySQL 8.0+ instalado e rodando
- ✔️ Git instalado

### Passo 1: Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/TechEventsRegistration.git
cd TechEventsRegistration
```

### Passo 2: Configurar Banco de Dados MySQL

Abra o MySQL e execute:

```sql
-- Criar banco de dados
CREATE DATABASE tech_events CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verificar criação
SHOW DATABASES;
```

### Passo 3: Configurar Conexão do Banco

Edite o arquivo `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tech_events?createDatabaseIfNotExist=true
    username: root
    password: sua_senha_aqui
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update  # Cria/atualiza tabelas automaticamente
    show-sql: true
```

### Passo 4: Instalar Dependências

```bash
mvn clean install
```

---

## 🚀 Como Executar

### Opção 1: Executar via Maven

```bash
mvn spring-boot:run
```

A aplicação iniciará em `http://localhost:8080`

### Opção 2: Build e Executar JAR

```bash
# Build
mvn clean package

# Executar o JAR
java -jar target/TechEventsRegistration-0.0.1-SNAPSHOT.jar
```

### Opção 3: Executar via IDE

1. Abra no IntelliJ IDEA ou Eclipse
2. Clique em `Run` → `Run 'TechEventsRegistrationApplication'`

---

## 📡 Endpoints da API

### Base URL
```
http://localhost:8080/api
```

### 🎤 Events (Eventos)

#### Listar todos os eventos (Paginado)
```http
GET /Events?page=0&size=6
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "eventName": "Spring Boot Workshop",
      "description": "Aprenda Spring Boot do zero",
      "local": "São Paulo",
      "category": "Backend",
      "vacancies": 50,
      "eventDateAndHours": "15/06/2026 14:30"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 6,
    "totalElements": 1
  }
}
```

#### Buscar evento por ID
```http
GET /Events/{id}
```

**Exemplo:**
```
GET /Events/1
```

#### Buscar eventos por nome
```http
GET /Events/find?name=Spring
```

#### Criar novo evento
```http
POST /Events
Content-Type: application/json

{
  "eventName": "React Workshop",
  "description": "Aprenda React do zero",
  "local": "Rio de Janeiro",
  "category": "Frontend",
  "vacancies": 40,
  "eventDateAndHours": "20/06/2026 15:00"
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "eventName": "React Workshop",
  "description": "Aprenda React do zero",
  "local": "Rio de Janeiro",
  "category": "Frontend",
  "vacancies": 40,
  "eventDateAndHours": "20/06/2026 15:00"
}
```

#### Atualizar evento
```http
PUT /Events
Content-Type: application/json

{
  "id": 1,
  "eventName": "Spring Boot Workshop - Atualizado",
  "description": "Aprenda Spring Boot avançado",
  "local": "São Paulo",
  "category": "Backend",
  "vacancies": 100,
  "eventDateAndHours": "25/06/2026 14:30"
}
```

#### Deletar evento
```http
DELETE /Events/{id}
```

**Response (204 No Content)** - Sem corpo

---

### 👥 Participants (Participantes)

#### Listar todos os participantes (Paginado)
```http
GET /Participants?page=0&size=6
```

#### Buscar participante por ID
```http
GET /Participants/{id}
```

#### Buscar participantes por nome
```http
GET /Participants/find?name=João
```

#### Criar novo participante
```http
POST /Participants
Content-Type: application/json

{
  "participantName": "João Silva",
  "email": "joao@example.com",
  "phoneNumber": "11987654321",
  "institution": "Universidade ABC"
}
```

#### Atualizar participante
```http
PUT /Participants
Content-Type: application/json

{
  "id": 1,
  "participantName": "João Silva Updated",
  "email": "joao.updated@example.com",
  "phoneNumber": "11987654321",
  "institution": "Universidade XYZ"
}
```

#### Deletar participante
```http
DELETE /Participants/{id}
```

---

### 📝 Enrollments (Inscrições)

#### Buscar inscrição por ID
```http
GET /Enrollments/{id}
```

#### Criar nova inscrição
```http
POST /Enrollments
Content-Type: application/json

{
  "participantId": 1,
  "eventId": 1,
  "enrollmentDate": "12/06/2026"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "participant": {
    "id": 1,
    "participantName": "João Silva",
    "email": "joao@example.com",
    "phoneNumber": "11987654321",
    "institution": "Universidade ABC"
  },
  "event": {
    "id": 1,
    "eventName": "Spring Boot Workshop",
    "description": "Aprenda Spring Boot do zero",
    "local": "São Paulo",
    "category": "Backend",
    "vacancies": 50,
    "eventDateAndHours": "15/06/2026 14:30"
  },
  "enrollmentDate": "12/06/2026"
}
```

#### Atualizar inscrição
```http
PUT /Enrollments
Content-Type: application/json

{
  "id": 1,
  "participantId": 1,
  "eventId": 2,
  "enrollmentDate": "13/06/2026"
}
```

#### Deletar inscrição
```http
DELETE /Enrollments/{id}
```

---

## 📂 Estrutura do Projeto

```
TechEventsRegistration/
├── src/
│   ├── main/
│   │   ├── java/com/jonas/TechEventsRegistration/
│   │   │   ├── TechEventsRegistrationApplication.java
│   │   │   ├── Configurer/
│   │   │   │   └── MvcConfigurer/
│   │   │   │       └── MvcConfigurer.java              # Configuração de Paginação
│   │   │   ├── Controllers/
│   │   │   │   ├── EventController.java               # REST endpoints de Events
│   │   │   │   ├── ParticipantController.java         # REST endpoints de Participants
│   │   │   │   └── EnrollmentController.java          # REST endpoints de Enrollments
│   │   │   ├── Services/
│   │   │   │   ├── EventServices.java                 # Lógica de Events
│   │   │   │   ├── ParticipantServices.java           # Lógica de Participants
│   │   │   │   └── EnrollmentServices.java            # Lógica de Enrollments
│   │   │   ├── Repository/
│   │   │   │   ├── EventRepository.java               # Query customizadas de Events
│   │   │   │   ├── ParticipantRepository.java         # Query customizadas de Participants
│   │   │   │   └── EnrollmentRepository.java          # Query customizadas de Enrollments
│   │   │   ├── Entity/
│   │   │   │   ├── Event.java                         # Entidade Event
│   │   │   │   ├── Participant.java                   # Entidade Participant
│   │   │   │   └── Enrollment.java                    # Entidade Enrollment (Junction)
│   │   │   ├── DTO/
│   │   │   │   ├── EventRequests/
│   │   │   │   │   ├── EventPostRequest.java          # DTO para POST
│   │   │   │   │   └── EventPutRequest.java           # DTO para PUT
│   │   │   │   ├── ParticipantRequest/
│   │   │   │   │   ├── ParticipantPostRequest.java    # DTO para POST
│   │   │   │   │   └── ParticipantPutRequest.java     # DTO para PUT
│   │   │   │   └── EnrollmentRequests/
│   │   │   │       ├── EnrollmentPostRequest.java     # DTO para POST
│   │   │   │       └── EnrollmentPutRequest.java      # DTO para PUT
│   │   │   ├── Mappers/
│   │   │   │   ├── EventMapper.java                   # Conversão Event ↔ DTO
│   │   │   │   ├── ParticipantMapper.java             # Conversão Participant ↔ DTO
│   │   │   │   └── EnrollmentMapper.java              # Conversão Enrollment ↔ DTO
│   │   │   ├── Exceptions/
│   │   │   │   ├── NotFoundException.java              # Exceção para recurso não encontrado
│   │   │   │   ├── BadRequestException.java            # Exceção para requisição inválida
│   │   │   │   └── ExceptionDetails/
│   │   │   │       └── ExceptionDetails.java           # Classe padronizada de erro
│   │   │   └── ExceptionsHandler/
│   │   │       └── ExceptionsHandler.java              # Handler centralizado de exceções
│   │   └── resources/
│   │       └── application.yaml                       # Configurações da aplicação
│   └── test/
│       └── java/com/jonas/TechEventsRegistration/
│           └── TechEventsRegistrationApplicationTests.java
├── pom.xml                                            # Dependências Maven
├── mvnw e mvnw.cmd                                    # Maven Wrapper
└── README.md                                          # Este arquivo
```

---

## 📊 Diagrama ER (Entidade-Relacionamento)

```
┌──────────────────┐           ┌──────────────────┐
│      Event       │           │   Participant    │
├──────────────────┤           ├──────────────────┤
│ PK: id           │◄─────────►│ PK: id           │
│    eventName     │  1   *    │    participantName│
│    description   │(Enrollment)│    email        │
│    local         │           │    phoneNumber   │
│    category      │           │    institution   │
│    vacancies     │           │                  │
│    eventDateAndHours          │                  │
└──────────────────┘           └──────────────────┘
        ▲                               ▲
        │                               │
        └───────────┬────────────────────┘
                    │
            ┌───────────────────┐
            │    Enrollment     │
            ├───────────────────┤
            │ PK: id            │
            │ FK: participant_id│
            │ FK: event_id      │
            │    enrollmentDate │
            └───────────────────┘
```

### Relacionamentos

- **Event (1) ← → (N) Enrollment** - Um evento pode ter múltiplas inscrições
- **Participant (1) ← → (N) Enrollment** - Um participante pode se inscrever em múltiplos eventos
- **Enrollment (N) ← → (N)** - Tabela de junção entre Event e Participant

---

## 📊 Status do Desenvolvimento

### ✅ Concluído

- [x] Arquitetura 3 camadas (Controller → Service → Repository)
- [x] CRUD completo para Event, Participant e Enrollment
- [x] Paginação de resultados
- [x] Tratamento centralizado de exceções
- [x] DTOs separados para POST e PUT
- [x] Mapeamento de objetos com MapStruct
- [x] Validação de email em Participant
- [x] Queries customizadas (find by name)
- [x] Testes de endpoints no Postman

### 🟡 Em Desenvolvimento

- [ ] Adicionar validações robustas (NotNull, NotBlank, Size, Pattern)
- [ ] Implementar logging profissional (SLF4J)
- [ ] Testes unitários (JUnit 5 + Mockito)
- [ ] Testes de integração
- [ ] API Versioning (v1, v2)
- [ ] Documentação Swagger/OpenAPI
- [ ] Spring Security (Autenticação + Autorização)
- [ ] Cache (Redis)

### ⚪ Planejado

- [ ] JWT Token
- [ ] OAuth2
- [ ] Async Operations
- [ ] Message Queue (RabbitMQ)
- [ ] Monitoring (Actuator, Prometheus)
- [ ] Docker & Docker Compose
- [ ] CI/CD Pipeline

---

## 🚀 Próximas Melhorias

### Priority 1 - Crítico
1. **Validação Robusta** - Adicionar @NotNull, @NotBlank, @Size em todos os DTOs
2. **Logging Profissional** - Implementar SLF4J com Logback
3. **Testes Unitários** - Coverage mínimo de 80% com JUnit 5

### Priority 2 - Importante
4. **Spring Security** - Authentication e Authorization
5. **Swagger/OpenAPI** - Documentação automática de API
6. **Query Optimization** - Índices no banco e lazy loading

### Priority 3 - Polimento
7. **Async Operations** - CompletableFuture para operações longas
8. **Cache Layer** - Redis para dados frequentes
9. **Containerization** - Docker e Docker Compose

---

## 🧪 Testando com Postman

### Instalação do Postman

1. Baixe em: https://www.postman.com/downloads/
2. Instale e abra a aplicação
3. Crie uma nova workspace

### Importar Collection

Todos os endpoints estão disponíveis para teste. Aqui está um passo-a-passo:

#### 1. Criar um Evento
```
POST http://localhost:8080/Events
Content-Type: application/json

{
  "eventName": "Java Masterclass",
  "description": "Aprenda Java avançado",
  "local": "São Paulo",
  "category": "Backend",
  "vacancies": 30,
  "eventDateAndHours": "20/06/2026 18:00"
}
```

#### 2. Criar um Participante
```
POST http://localhost:8080/Participants
Content-Type: application/json

{
  "participantName": "Maria Santos",
  "email": "maria@example.com",
  "phoneNumber": "11999887766",
  "institution": "UFRJ"
}
```

#### 3. Criar uma Inscrição
```
POST http://localhost:8080/Enrollments
Content-Type: application/json

{
  "participantId": 1,
  "eventId": 1,
  "enrollmentDate": "12/06/2026"
}
```

#### 4. Buscar Eventos
```
GET http://localhost:8080/api/Events?page=0&size=6
```

### Dicas de Teste

- **Variáveis de Ambiente:** Use `{{{{ baseUrl }}}}` nas requests
- **Tests:** Adicione scripts para validar Status Code, Response Time
- **Assertions:** Verifique que status é 201 (Created), 200 (OK), 404 (Not Found)
- **Data Types:** JSON sempre com `Content-Type: application/json`

---

## 💾 Banco de Dados MySQL

### Schema Criado Automaticamente

Ao iniciar a aplicação com `ddl-auto: update`, as tabelas são criadas automaticamente:

```sql
-- Tabela Event
CREATE TABLE event (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  event_name VARCHAR(255),
  description VARCHAR(255),
  local VARCHAR(255),
  category VARCHAR(255),
  vacancies INT,
  event_date_and_hours DATETIME
);

-- Tabela Participant
CREATE TABLE participant (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  participant_name VARCHAR(255),
  email VARCHAR(255),
  phone_number VARCHAR(255),
  institution VARCHAR(255)
);

-- Tabela Enrollment
CREATE TABLE enrollment (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  participant_id BIGINT,
  event_id BIGINT,
  enrollment_date DATE,
  FOREIGN KEY (participant_id) REFERENCES participant(id),
  FOREIGN KEY (event_id) REFERENCES event(id)
);
```

### Consultas Úteis

```sql
-- Ver todos os eventos
SELECT * FROM event;

-- Ver todos os participantes
SELECT * FROM participant;

-- Ver todas as inscrições
SELECT * FROM enrollment;

-- Ver inscrições de um evento
SELECT p.participant_name, p.email, e.enrollment_date 
FROM enrollment e
JOIN participant p ON e.participant_id = p.id
WHERE e.event_id = 1;
```

---

## 🔧 Configuração do Ambiente

### application.yaml

Arquivo principal de configuração:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tech_events?createDatabaseIfNotExist=true
    username: root
    password: sua_senha
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update  # create / create-drop / update / validate
    show-sql: true     # Mostra SQL no console (desabilitar em produção)
```

### Variáveis Conforme Ambiente

- **Desenvolvimento:** `show-sql: true`, `ddl-auto: update`
- **Teste:** `ddl-auto: create-drop`, banco H2
- **Produção:** `show-sql: false`, `ddl-auto: validate`

---

## 🤝 Contribuindo

Este é um projeto **pessoal de estudos**, mas contribuições e sugestões são bem-vindas!

### Como Contribuir

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### Reportar Bugs

Para reportar bugs, abra uma **Issue** descrevendo:
- Comportamento esperado
- Comportamento atual
- Passos para reproduzir
- Versão do Java/Spring Boot

---

## 📝 Licença

Este projeto é fornecido sem licença formal. Use livremente para fins educacionais.

---

## 👨‍💻 Autor

**Jonas** 
- GitHub: [jonathanleao](https://github.com/seu-usuario)
- Email: jonathan.oliveirajunior.23@gmail.com

---

## 📚 Referências e Recursos

### Documentação Oficial
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Validation](https://spring.io/guides/gs/validating-form-input/)

### Práticas e Padrões
- [RESTful API Best Practices](https://restfulapi.net/)
- [Java Design Patterns](https://refactoring.guru/design-patterns/java)
- [Clean Code - Robert Martin](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)

### Ferramentas
- [Postman Learning Center](https://learning.postman.com/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

## 🎓 Objetivo do Projeto

Este projeto foi desenvolvido com o objetivo de **praticar e aprender**:

✨ Conceitos de desenvolvimento de APIs REST  
✨ Arquitetura em camadas  
✨ Good practices em Java e Spring Boot  
✨ Manipulação de banco de dados relacionais  
✨ Padrões de design (Repository, Service, DTO, Mapper)  
✨ Tratamento de erros e exceções  
✨ Testes de endpoints  

---

## ⭐ Se Gostou, Deixe uma Star!

Se este projeto te ajudou a aprender algo novo, considere deixar uma ⭐ no GitHub!

---

**Última atualização:** 12 de Junho de 2026  
**Versão:** 0.0.1-SNAPSHOT  
**Status:** 🟡 Em Desenvolvimento

