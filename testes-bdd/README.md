# Módulo de testes BDD

Este módulo testa as rotas da API ESG por HTTP. Os cenários estão em Gherkin e usam Cucumber, Rest Assured e schemas JSON.

## Cenários

Os sete cenários cobrem cadastro e consulta de empresas, emissões, licenças, auditorias, autenticação, respostas de erro e endpoints de saúde da aplicação. As verificações incluem status HTTP, corpo JSON e contrato JSON Schema.

## Executar os testes

Na pasta raiz do repositório, copie o arquivo de exemplo e configure as senhas do banco e o segredo JWT:

```powershell
Copy-Item .env.example .env
```

Com Docker Desktop iniciado, execute:

```powershell
.\testes-bdd\run-tests.ps1
```

O script inicia Oracle, API e testes, e encerra os serviços ao final. O comando deve ser executado na raiz do repositório.

Para iniciar os serviços manualmente:

```powershell
docker compose -f docker-compose.yml -f testes-bdd/docker-compose.test.yml up --build --abort-on-container-exit --exit-code-from bdd
docker compose -f docker-compose.yml -f testes-bdd/docker-compose.test.yml down
```

Os relatórios ficam em `testes-bdd/target/`. Os cenários ficam em `src/test/resources/features/` e os contratos em `src/test/resources/schemas/`.

## Integração contínua

O GitHub Actions executa os testes BDD em um Oracle temporário. A execução pode ser consultada na aba Actions do repositório; os relatórios são publicados como artefato do workflow.
