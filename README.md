# Testes BDD da API ESG

Este projeto contém testes automatizados para a API ESG Compliance, desenvolvida em Java com Spring Boot. Os cenários foram escritos em Gherkin e executados com Cucumber e Rest Assured.

## O que foi feito

- Foram criados sete cenários para empresas, emissões de carbono, licenças ambientais, auditorias, autenticação, validações negativas e monitoramento da aplicação.
- As chamadas verificam status HTTP e conteúdo JSON.
- Foram criados schemas JSON para conferir o formato das respostas.
- O workflow do GitHub Actions inicia um banco Oracle, executa a API e roda os cenários BDD.

## Tecnologias

Java 21 para o módulo de testes, Maven, Cucumber, Gherkin, Rest Assured e JSON Schema. A API usa Java 24, Spring Boot e Oracle.

## Como executar

É necessário ter Docker Desktop com Compose instalado e iniciado. Na raiz do repositório, crie o arquivo de configuração:

```powershell
Copy-Item .env.example .env
```

Preencha no `.env` as senhas do Oracle e o valor de `JWT_SECRET`. Depois execute os testes:

```powershell
.\testes-bdd\run-tests.ps1
```

O script inicia o banco, a API e o executor de testes. Ao final, encerra os serviços e retorna o resultado dos testes.

Também é possível executar com Docker Compose:

```powershell
docker compose -f docker-compose.yml -f testes-bdd/docker-compose.test.yml up --build --abort-on-container-exit --exit-code-from bdd
docker compose -f docker-compose.yml -f testes-bdd/docker-compose.test.yml down
```

Os relatórios são gravados em `testes-bdd/target/`. O arquivo de cenários fica em `testes-bdd/src/test/resources/features/api_esg.feature` e os schemas ficam em `testes-bdd/src/test/resources/schemas/`.

## Execução no GitHub Actions

O workflow fica em `.github/workflows/ci-cd.yml`. Ele compila a API, inicia um Oracle temporário e executa os testes BDD. Os relatórios ficam disponíveis como artefato da execução do workflow.
