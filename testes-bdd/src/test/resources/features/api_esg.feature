# language: pt
Funcionalidade: API de governança e compliance ambiental
  Como equipe de qualidade da plataforma ESG
  Quero validar as APIs por HTTP com contratos JSON
  Para manter rastreáveis empresas, emissões, licenças e auditorias

  Cenário: Criar, consultar, atualizar e remover empresa
    Quando envio POST para "/api/empresas" com JSON:
      """
      {"nome":"Empresa ESG BDD","cnpj":"{cnpj}","setor":"Energia renovável","email":"bdd-{nonce}@example.com","telefone":"(11) 3000-0000","ativa":true}
      """
    Então o status HTTP deve ser 201
    E a resposta deve obedecer ao schema "empresa.json"
    E guardo o campo "id" da resposta como "empresa_criada_id"
    Quando envio GET para "/api/empresas/{empresa_criada_id}"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "empresa.json"
    Quando envio PUT para "/api/empresas/{empresa_criada_id}" com JSON:
      """
      {"nome":"Empresa ESG BDD Atualizada","cnpj":"{cnpj}","setor":"Energia limpa","email":"bdd-{nonce}@example.com","telefone":"(11) 3000-0001","ativa":true}
      """
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "empresa.json"
    Quando envio GET para "/api/empresas"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "empresas.json"
    Quando envio DELETE para "/api/empresas/{empresa_criada_id}"
    Então o status HTTP deve ser 204

  Cenário: Registrar emissões de carbono e conferir o total por empresa
    Quando envio POST para "/api/empresas/{empresa_id}/emissoes" com JSON:
      """
      {"quantidadeCo2":12.5,"unidade":"tCO2e","fonte":"Consumo de energia","dataRegistro":"2026-01-15","periodoRef":"2026-01","compensado":false,"observacoes":"Medição automatizada"}
      """
    Então o status HTTP deve ser 201
    E a resposta deve obedecer ao schema "emissao.json"
    E guardo o campo "id" da resposta como "emissao_id"
    Quando envio GET para "/api/empresas/{empresa_id}/emissoes"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "emissoes.json"
    Quando envio GET para "/api/emissoes/{emissao_id}"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "emissao.json"
    Quando envio GET para "/api/empresas/{empresa_id}/emissoes/total"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "total-emissao.json"
    E o campo "totalCo2" da resposta deve ser 12.5
    Quando envio PUT para "/api/emissoes/{emissao_id}" com JSON:
      """
      {"quantidadeCo2":10.0,"fonte":"Energia solar","dataRegistro":"2026-01-15","periodoRef":"2026-01","compensado":true,"observacoes":"Compensada"}
      """
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "emissao.json"
    Quando envio DELETE para "/api/emissoes/{emissao_id}"
    Então o status HTTP deve ser 204

  Cenário: Gerenciar licenças ambientais e consultar vencimentos
    Quando envio POST para "/api/empresas/{empresa_id}/licencas" com JSON:
      """
      {"numeroLicenca":"BDD-LIC-{nonce}","tipo":"Operação","orgaoEmissor":"IBAMA","dataEmissao":"2025-01-01","dataValidade":"2027-12-31","status":"ATIVA","observacoes":"Licença de teste"}
      """
    Então o status HTTP deve ser 201
    E a resposta deve obedecer ao schema "licenca.json"
    E guardo o campo "id" da resposta como "licenca_id"
    Quando envio GET para "/api/empresas/{empresa_id}/licencas"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "licencas.json"
    Quando envio GET para "/api/licencas/{licenca_id}"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "licenca.json"
    Quando envio GET para "/api/licencas/vencendo?dias=3650"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "licencas.json"
    Quando envio PUT para "/api/licencas/{licenca_id}" com JSON:
      """
      {"tipo":"Operação atualizada","orgaoEmissor":"IBAMA","dataValidade":"2028-12-31","status":"ATIVA","observacoes":"Renovada"}
      """
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "licenca.json"
    Quando envio DELETE para "/api/licencas/{licenca_id}"
    Então o status HTTP deve ser 204

  Cenário: Rastrear auditoria ESG de uma empresa
    Quando envio POST para "/api/empresas/{empresa_id}/auditorias" com JSON:
      """
      {"titulo":"Auditoria ESG BDD","tipo":"Ambiental","auditor":"Equipe de qualidade","dataAuditoria":"2026-02-10","resultado":"CONFORME","score":95.5,"relatorio":"Verificação de controles ambientais"}
      """
    Então o status HTTP deve ser 201
    E a resposta deve obedecer ao schema "auditoria.json"
    E guardo o campo "id" da resposta como "auditoria_id"
    Quando envio GET para "/api/empresas/{empresa_id}/auditorias"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "auditorias.json"
    Quando envio GET para "/api/auditorias/{auditoria_id}"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "auditoria.json"
    Quando envio PUT para "/api/auditorias/{auditoria_id}" com JSON:
      """
      {"titulo":"Auditoria ESG BDD atualizada","tipo":"Ambiental","auditor":"Equipe de qualidade","dataAuditoria":"2026-02-10","resultado":"PARCIALMENTE_CONFORME","score":80,"relatorio":"Plano de ação registrado"}
      """
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "auditoria.json"
    Quando envio DELETE para "/api/auditorias/{auditoria_id}"
    Então o status HTTP deve ser 204

  Cenário: Rejeitar emissões inválidas e acessos sem autenticação
    Quando envio POST sem autenticação para "/api/empresas/{empresa_id}/emissoes" com JSON:
      """
      {"quantidadeCo2":-1,"fonte":"","dataRegistro":"2026-01-15","periodoRef":"2026-99"}
      """
    Então o status HTTP deve ser 401
    Quando envio POST autenticado para "/api/empresas/{empresa_id}/emissoes" com JSON:
      """
      {"quantidadeCo2":-1,"fonte":"","dataRegistro":"2026-01-15","periodoRef":"2026-99"}
      """
    Então o status HTTP deve ser 400
    E a resposta deve obedecer ao schema "erro-validacao.json"
    Quando envio GET para "/api/empresas/99999999"
    Então o status HTTP deve ser 404
    E a resposta deve obedecer ao schema "erro.json"

  Cenário: Registrar usuário e autenticar credenciais válidas
    Quando envio POST público para "/api/auth/registro" com JSON:
      """
      {"nome":"Usuário BDD","email":"bdd-{nonce}@example.com","senha":"TesteSeguro123!"}
      """
    Então o status HTTP deve ser 201
    E a resposta deve obedecer ao schema "usuario.json"
    Quando envio POST público para "/api/auth/login" com JSON:
      """
      {"email":"bdd-{nonce}@example.com","senha":"TesteSeguro123!"}
      """
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "token.json"

  Cenário: Consultar endpoints de saúde e informação da aplicação
    Quando envio GET sem autenticação para "/actuator/health"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "health.json"
    Quando envio GET para "/actuator/info"
    Então o status HTTP deve ser 200
    E a resposta deve obedecer ao schema "actuator-info.json"
