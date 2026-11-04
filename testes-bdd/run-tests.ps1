$ErrorActionPreference = "Stop"
$repositoryRoot = Split-Path -Parent $PSScriptRoot
Push-Location $repositoryRoot
try {
    if (-not (Test-Path -LiteralPath ".env")) {
        throw "Crie .env a partir de .env.example e configure as credenciais do Oracle e JWT_SECRET."
    }

    docker compose -f docker-compose.yml -f testes-bdd/docker-compose.test.yml up --build --abort-on-container-exit --exit-code-from bdd
    $testExitCode = $LASTEXITCODE
    docker compose -f docker-compose.yml -f testes-bdd/docker-compose.test.yml down
    exit $testExitCode
}
finally {
    Pop-Location
}
