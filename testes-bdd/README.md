# Testes automatizados BDD - ESG Compliance API

Projeto de testes escrito em Java 21, com Cucumber/Gherkin, Rest Assured e JSON Schema. Os testes são executados por HTTP contra a API Spring Boot e cobrem todos os métodos dos controllers: autenticação, empresas, emissões de carbono, licenças ambientais e auditorias.

## Pré-requisitos

- Java 21 ou superior e Maven 3.9+ para executar localmente.
- Docker Desktop com Compose v2 para subir a API, Oracle e executor de testes em conjunto.
- As variáveis `ORACLE_PASSWORD`, `ORACLE_APP_PASSWORD` e `JWT_SECRET` preenchidas no `.env` usado pelo Compose.
- Usuário de seed disponível (`admin@esg.com` / `admin123` por padrão). Credenciais podem ser sobrescritas pelas variáveis `ESG_ADMIN_EMAIL` e `ESG_ADMIN_PASSWORD`.

## Execução local

Na raiz do repositório, inicie a API e o banco:

```powershell
Copy-Item .env.example .env
# Preencha ORACLE_PASSWORD, ORACLE_APP_PASSWORD e JWT_SECRET em .env.
docker compose up --build -d
```

Depois, em outro terminal:

```powershell
cd testes-bdd
$env:ESG_API_URL = "http://localhost:8080"
$env:ESG_ADMIN_EMAIL = "admin@esg.com"
$env:ESG_ADMIN_PASSWORD = "admin123"
mvn -B test
```

O relatório XML Cucumber é criado em `target/cucumber-reports/cucumber.xml`; os relatórios Surefire ficam em `target/surefire-reports/`.

Para conferir apenas o parsing do Gherkin e a associação dos passos, sem enviar chamadas HTTP:

```powershell
mvn -B test '-Dcucumber.execution.dry-run=true'
```

Esse modo não substitui a execução integrada contra a API.

## Pipeline do GitHub Actions

O workflow `.github/workflows/ci-cd.yml` também executa o job `bdd-api-tests` em pushes e pull requests. O job inicia um Oracle temporário, compila e sobe a API Spring Boot, roda `mvn -B -f testes-bdd/pom.xml test` e publica os relatórios Cucumber/Surefire como artefato `relatorios-testes-bdd`. A publicação da imagem Docker depende da aprovação tanto dos testes existentes quanto do job BDD.

## Execução integrada por Docker Compose

Na raiz do repositório:

```powershell
Copy-Item .env.example .env
# Preencha as três variáveis obrigatórias no arquivo .env.
docker compose -f docker-compose.yml -f testes-bdd/docker-compose.test.yml up --build --abort-on-container-exit --exit-code-from bdd
```

O serviço `bdd` aguarda a API ficar saudável antes de executar os cenários. O código de saída do comando reflete o resultado dos testes. Os relatórios ficam em `testes-bdd/target/`. Para encerrar os serviços:

```powershell
docker compose -f docker-compose.yml -f testes-bdd/docker-compose.test.yml down
```

Como alternativa, rode `.\testes-bdd\run-tests.ps1` na raiz. O script executa o Compose e encerra os serviços após a conclusão.

## Cenários e cobertura

| Cenário Gherkin | APIs verificadas |
|---|---|
| CRUD de empresa | `GET/POST/PUT/DELETE /api/empresas` e `GET /api/empresas/{id}` |
| Emissões de carbono | criação, listagem por empresa, detalhe, total, atualização e exclusão |
| Licenças ambientais | criação, listagem por empresa, detalhe, vencimentos, atualização e exclusão |
| Auditorias ESG | criação, listagem por empresa, detalhe, atualização e exclusão |
| Respostas negativas | acesso sem token (401), dados inválidos (400) e recurso inexistente (404) |
| Autenticação | registro (`201`) e login (`200`) |

São 24 combinações de método e rota identificadas nos controllers, mais os endpoints Actuator `/actuator/health` e `/actuator/info`. As respostas JSON de sucesso e erro são verificadas contra contratos em `src/test/resources/schemas/`, além da validação dos status HTTP. Os cenários estão em `src/test/resources/features/api_esg.feature`.

Cada cenário autentica com o usuário configurado e cria uma empresa fixture com identificadores únicos. Recursos de domínio são removidos no próprio cenário; o hook remove a empresa fixture ao final.

## Evidência da execução

O resultado do terminal e os XML em `target/` são evidências geradas pelo executor. Para a entrega acadêmica, capture a tela com o resumo dos cenários executados e inclua a captura junto do XML. A documentação do projeto explica a execução, mas não contém uma captura simulada de uma execução ainda não realizada.
