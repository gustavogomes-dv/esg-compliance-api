-- V2__seed_data.sql
-- Dados iniciais para teste do sistema

-- Usuário admin (senha: admin123 - em produção usar hash BCrypt real)
INSERT INTO usuario (nome, email, senha, role)
VALUES ('Administrador', 'admin@esg.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'ADMIN');

-- Usuário padrão (senha: user123)
INSERT INTO usuario (nome, email, senha, role)
VALUES ('Analista ESG', 'analista@esg.com',
        '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNv4N2SzfI04MUlQiLa',
        'USER');

-- Empresas de exemplo
INSERT INTO empresa (nome, cnpj, setor, email, telefone)
VALUES ('Petróleo Verde S.A.', '12.345.678/0001-90', 'Energia', 'contato@petroverde.com', '(11) 3000-0001');

INSERT INTO empresa (nome, cnpj, setor, email, telefone)
VALUES ('AgroSustentável Ltda.', '98.765.432/0001-10', 'Agronegócio', 'esg@agrosust.com.br', '(11) 3000-0002');

INSERT INTO empresa (nome, cnpj, setor, email, telefone)
VALUES ('Indústria Limpa S.A.', '11.222.333/0001-44', 'Indústria', 'compliance@industlimpa.com', '(11) 3000-0003');

COMMIT;
