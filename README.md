# Projeto - Cidades ESG Inteligentes

API REST de governança e compliance ambiental (ESG), feita com Spring Boot. Ela cadastra empresas, licenças ambientais, emissões de carbono e auditorias, e protege os endpoints com autenticação JWT.

Nesta etapa do trabalho da fase "Navegando pelo mundo DevOps", a API ganhou:

- Dockerfile com build em duas etapas
- docker-compose com a aplicação e o banco Oracle
- pipeline CI/CD no GitHub Actions (build, testes, imagem, staging e produção)
- deploy em uma VM no Azure, com dois ambientes separados

**Integrante:** Gustavo (RM566105), FIAP, Análise e Desenvolvimento de Sistemas

## Como executar localmente com Docker

Pré-requisito: Docker Desktop instalado e aberto.

1. Clone o repositório e entre na pasta do projeto:

```bash
git clone https://github.com/gustavogomes-dv/esg-compliance-api.git
cd esg-compliance-api
```

2. Crie o arquivo `.env` a partir do modelo. No PowerShell:

```powershell
Copy-Item .env.example .env
```

No Linux ou macOS:

```bash
cp .env.example .env
```

3. Abra o `.env` e troque as senhas e o `JWT_SECRET` por valores seus. O `JWT_SECRET` precisa ter pelo menos 32 caracteres. O Compose recusa subir se alguma dessas variáveis estiver vazia.

4. Suba os containers:

```bash
docker compose up --build -d
```

5. Acompanhe até os dois serviços ficarem `healthy`. Na primeira vez o Oracle leva alguns minutos para iniciar:

```bash
docker compose ps
```

6. Teste a aplicação:

```bash
curl http://localhost:8080/actuator/health
```

Resposta esperada: `{"status":"UP"}`

Para parar tudo:

```bash
docker compose down
```

Para apagar também os dados do banco, use `docker compose down -v`.

### Variáveis do `.env`

| Variável | Para que serve |
|---|---|
| `APP_PORT` | Porta da API no host (padrão 8080) |
| `ORACLE_PORT` | Porta do Oracle no host (padrão 1521) |
| `ORACLE_PASSWORD` | Senha do administrador do Oracle |
| `ORACLE_APP_USER` | Usuário que a aplicação usa no banco (`esg`) |
| `ORACLE_APP_PASSWORD` | Senha do usuário da aplicação |
| `JWT_SECRET` | Segredo usado para assinar os tokens JWT |

## Pipeline CI/CD

O pipeline está em `.github/workflows/ci-cd.yml` e usa GitHub Actions.

**Gatilhos**

- `push` na `main` ou `master`: roda o pipeline inteiro
- `pull_request`: roda só o build e os testes
- `workflow_dispatch`: execução manual pela aba Actions

**Jobs**

| Ordem | Job | O que faz |
|---|---|---|
| 1 | `build-test` | Instala o Java 24, usa cache do Maven e roda `mvn -B verify` (compila e executa os testes JUnit) |
| 2 | `image` | Constrói a imagem pelo Dockerfile e publica no GitHub Container Registry (GHCR) com a tag `sha-<commit>` |
| 3 | `deploy-staging` | Conecta na VM por SSH, baixa a imagem nova, sobe a aplicação em `/opt/esg/staging` e espera o container ficar `healthy` |
| 4 | `deploy-production` | Só roda depois do staging dar certo e depois da aprovação manual. Faz o mesmo deploy em `/opt/esg/production` |

**Detalhes importantes**

- Cada job só começa se o anterior terminou com sucesso. Se os testes falham, nada é publicado.
- A produção usa o environment `production` do GitHub com **Required reviewers**, então alguém precisa aprovar antes do deploy.
- Os dados do servidor ficam em secrets de cada environment (`DEPLOY_HOST`, `DEPLOY_USER`, `DEPLOY_SSH_KEY`, `DEPLOY_PATH`, `GHCR_TOKEN`). Nenhuma senha está no código.
- A espera pelo healthcheck tenta até 30 vezes, a cada 10 segundos. Se o container não ficar saudável, o job mostra os logs e falha.
- Staging e produção rodam na mesma VM, em pastas diferentes (`/opt/esg/staging` e `/opt/esg/production`). Cada pasta tem seu `docker-compose.yml` e seu `.env`, então containers, volumes, portas e senhas são separados.

| Ambiente | Porta da API | Porta do Oracle |
|---|---|---|
| Staging | 8081 | 1522 |
| Produção | 8080 | 1521 |

## Containerização

### Dockerfile

```dockerfile
# Stage 1: Build
FROM maven:3-eclipse-temurin-24 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
# Aqui eu crio um usuário sem privilégios para a aplicação não rodar como root
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /app/target/*.jar app.jar
USER spring:spring
EXPOSE 8080
# Aqui eu limito o uso de memória da JVM a 75% do que o container tem
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
```

### Estratégias usadas

- **Multi-stage build:** a primeira etapa tem Maven e JDK para compilar. A imagem final só leva o JRE e o `.jar`, então fica bem menor.
- **Cache das dependências:** o `pom.xml` é copiado e as dependências são baixadas antes de copiar o `src`. Se só o código mudar, o Docker reaproveita essa camada e o build é mais rápido.
- **Usuário sem privilégios:** a aplicação roda com o usuário `spring`, não como root.
- **Imagem base leve:** `eclipse-temurin:24-jre-alpine`.
- **Memória da JVM:** `-XX:MaxRAMPercentage=75.0` faz a JVM respeitar o limite do container.
- **`.dockerignore`:** deixa de fora `target/`, `.git`, `.idea` e `.env`, para o contexto de build ser pequeno e sem segredos.
- **Configuração por variáveis de ambiente:** URL do banco, usuário, senha e `JWT_SECRET` entram pelo `.env`, nunca dentro da imagem.
- **Tag por commit:** cada imagem publicada no GHCR leva `sha-<commit>`, o que permite saber qual versão está em cada ambiente.

### docker-compose

O `docker-compose.yml` define dois serviços:

- `oracle`: imagem `gvenzl/oracle-free:23-slim`, com o serviço `FREEPDB1` e o usuário da aplicação criado pelas variáveis `ORACLE_APP_USER` e `ORACLE_APP_PASSWORD`. A porta do Oracle é publicada só em `127.0.0.1`.
- `app`: a API Spring Boot, que só inicia depois que o Oracle estiver saudável.

Os dois ficam na rede `esg-net`, o banco guarda os dados no volume `oracle-data` e ambos têm healthcheck. A aplicação usa o endpoint `/actuator/health` do Spring Actuator.

## Prints do funcionamento

Os arquivos ficam na pasta `docs/prints/`.

### Execução local com Docker

![docker compose ps local](docs/prints/01-local-compose-ps.png)

![health local](docs/prints/02-local-health.png)

### Pipeline: build e testes

![testes no IntelliJ](docs/prints/03-testes-intellij.png)

![pipeline verde no Actions](docs/prints/04-actions-pipeline.png)

### Imagem publicada no GHCR

![imagem no GHCR](docs/prints/05-ghcr-imagem.png)

### Deploy em staging

![deploy staging](docs/prints/06-deploy-staging.png)

![health staging](docs/prints/07-health-staging.png)

### Deploy em produção

![aprovação da produção](docs/prints/08-aprovacao-producao.png)

![health produção](docs/prints/09-health-producao.png)

### Containers na VM

![docker compose ps na VM](docs/prints/10-vm-compose-ps.png)

## Tecnologias utilizadas

- Java 24 e Spring Boot 3.4.5
- Spring Security com JWT (jjwt)
- Spring Data JPA
- Flyway (migrations)
- Oracle Database Free 23
- Spring Boot Actuator (healthcheck)
- Maven
- JUnit 5
- Docker e Docker Compose
- GitHub Actions
- GitHub Container Registry (GHCR)
- Microsoft Azure (VM Ubuntu 24.04)

[x<img width="1907" height="1002" alt="deploy-production" src="https://github.com/user-attachments/assets/8e251032-147a-4137-bfd4-1f443aa77a23" />
<img width="1915" height="1012" alt="deploy-staging" src="https://github.com/user-attachments/assets/d7635a06-3aca-4ba6-9dff-60dcb62cc2ca" />
<img width="1877" height="302" alt="docker compose ps1" src="https://github.com/user-attachments/assets/f2de14e1-734a-447d-9362-532787c10b93" />
<img width="1917" height="1015" alt="docker local" src="https://github.com/user-attachments/assets/76a17cb6-95a7-44c0-8d16-754da6d4b124" />
<img width="1912" height="1007" alt="build e tests" src="https://github.com/user-attachments/assets/d5bbcb3a-a1e0-4238-9e85-d02f081ff18b" />
<img width="1910" height="1000" alt="docker compose ps " src="https://github.com/user-attachments/assets/f500c6ba-fb6f-4801-90f3-fa03b6154baf" />
<img width="1916" height="1016" alt="teste-api" src="https://github.com/user-attachments/assets/d3bce900-9594-41f7-b984-2dc24532d473" />
<img width="1911" height="1006" alt="porta 8080" src="https://github.com/user-attachments/assets/db451b9c-635f-4d40-b6fc-fe97fc22a16b" />

## Checklist de entrega

- [x] Arquivo .zip organizado
- [x] Dockerfile funcional
- [x] docker-compose.yml
- [x] Pipeline com build, teste e deploy
- [x] README com instruções e prints
- [x] Documentação técnica com evidências
- [x] Deploy em staging e produção
