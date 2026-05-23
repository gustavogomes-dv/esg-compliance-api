# ESG Compliance API

API RESTful para **Governança e Compliance Ambiental** desenvolvida com Java 17 e Spring Boot 3.

## Tema ESG
**Governança e Compliance Ambiental** — Registro de conformidade com normas ambientais, monitoramento de emissões de carbono, controle de licenças ambientais e alertas para renovação.

---

## Tecnologias

- Java 17
- Spring Boot 3.2
- Spring Security + JWT
- Spring Data JPA
- Oracle Database (via Docker)
- Flyway (migrations)
- Docker + Docker Compose
- Bean Validation
- Lombok

---

## Como rodar com Docker

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/esg-compliance-api.git
cd esg-compliance-api

# Subir Oracle + aplicação
docker-compose up --build
```

A API estará disponível em: `http://localhost:8080`

---

## Como rodar localmente (sem Docker)

Pré-requisitos: Java 17, Maven, Oracle rodando.

```bash
# Configurar variáveis de ambiente
export ORACLE_HOST=localhost
export ORACLE_PORT=1521
export ORACLE_SID=ORCL
export ORACLE_USER=system
export ORACLE_PASSWORD=oracle

# Rodar
mvn spring-boot:run
```

---

## Autenticação

A API usa JWT. Para acessar os endpoints protegidos:

### 1. Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@esg.com",
  "senha": "admin123"
}
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 2. Usar o token
Adicione o header em todas as requisições:
```
Authorization: Bearer <token>
```

---

## Endpoints

### Autenticação
| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | /api/auth/login | Realizar login | Não |
| POST | /api/auth/registro | Registrar usuário | Não |

### Empresas
| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | /api/empresas | Listar todas | Sim |
| GET | /api/empresas/{id} | Buscar por ID | Sim |
| POST | /api/empresas | Cadastrar empresa | Sim |
| PUT | /api/empresas/{id} | Atualizar empresa | Sim |
| DELETE | /api/empresas/{id} | Remover empresa | ADMIN |

### Licenças Ambientais
| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | /api/empresas/{id}/licencas | Listar por empresa | Sim |
| GET | /api/licencas/{id} | Buscar por ID | Sim |
| GET | /api/licencas/vencendo?dias=30 | Próximas do vencimento | Sim |
| POST | /api/empresas/{id}/licencas | Cadastrar licença | Sim |
| PUT | /api/licencas/{id} | Atualizar licença | Sim |
| DELETE | /api/licencas/{id} | Remover licença | ADMIN |

### Emissões de Carbono
| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | /api/empresas/{id}/emissoes | Histórico por empresa | Sim |
| GET | /api/emissoes/{id} | Buscar por ID | Sim |
| GET | /api/empresas/{id}/emissoes/total | Total de emissões | Sim |
| POST | /api/empresas/{id}/emissoes | Registrar emissão | Sim |
| PUT | /api/emissoes/{id} | Atualizar emissão | Sim |
| DELETE | /api/emissoes/{id} | Remover emissão | ADMIN |

### Auditorias
| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | /api/empresas/{id}/auditorias | Listar por empresa | Sim |
| GET | /api/auditorias/{id} | Buscar por ID | Sim |
| POST | /api/empresas/{id}/auditorias | Registrar auditoria | Sim |
| PUT | /api/auditorias/{id} | Atualizar auditoria | Sim |
| DELETE | /api/auditorias/{id} | Remover auditoria | ADMIN |

---

## Usuários padrão (seed)

| Email | Senha | Papel |
|-------|-------|-------|
| admin@esg.com | admin123 | ADMIN |
| analista@esg.com | user123 | USER |

---

## Banco de Dados (Flyway Migrations)

| Versão | Arquivo | Descrição |
|--------|---------|-----------|
| V1 | V1__create_tables.sql | Criação das tabelas |
| V2 | V2__seed_data.sql | Dados iniciais |

---

## Estrutura do Projeto

```
src/main/java/com/esg/compliance/
├── controller/      # Endpoints REST
├── service/         # Regras de negócio
├── repository/      # Acesso ao banco
├── model/           # Entidades JPA
├── security/        # JWT e filtros
├── config/          # Spring Security config
└── exception/       # Tratamento de erros
```
