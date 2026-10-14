-- V1__create_tables.sql
-- Criação das tabelas do sistema de Governança e Compliance Ambiental

CREATE TABLE empresa (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome        VARCHAR2(200)  NOT NULL,
    cnpj        VARCHAR2(18)   NOT NULL UNIQUE,
    setor       VARCHAR2(100)  NOT NULL,
    email       VARCHAR2(150)  NOT NULL,
    telefone    VARCHAR2(20),
    ativa       NUMBER(1)      DEFAULT 1 NOT NULL,
    criado_em   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE licenca_ambiental (
    id              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    empresa_id      NUMBER         NOT NULL,
    numero_licenca  VARCHAR2(50)   NOT NULL UNIQUE,
    tipo            VARCHAR2(100)  NOT NULL,
    orgao_emissor   VARCHAR2(150)  NOT NULL,
    data_emissao    DATE           NOT NULL,
    data_validade   DATE           NOT NULL,
    status          VARCHAR2(20)   DEFAULT 'ATIVA' NOT NULL,
    observacoes     VARCHAR2(500),
    criado_em       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_licenca_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE TABLE emissao_carbono (
    id              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    empresa_id      NUMBER          NOT NULL,
    quantidade_co2  NUMBER(10, 2)   NOT NULL,
    unidade         VARCHAR2(20)    DEFAULT 'tCO2e' NOT NULL,
    fonte           VARCHAR2(200)   NOT NULL,
    data_registro   DATE            NOT NULL,
    periodo_ref     VARCHAR2(7)     NOT NULL,
    compensado      NUMBER(1)       DEFAULT 0 NOT NULL,
    observacoes     VARCHAR2(500),
    criado_em       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_emissao_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE TABLE auditoria (
    id              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    empresa_id      NUMBER         NOT NULL,
    titulo          VARCHAR2(200)  NOT NULL,
    tipo            VARCHAR2(100)  NOT NULL,
    auditor         VARCHAR2(150)  NOT NULL,
    data_auditoria  DATE           NOT NULL,
    resultado       VARCHAR2(20)   NOT NULL,
    score           NUMBER(5, 2),
    relatorio       CLOB,
    criado_em       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_auditoria_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE TABLE usuario (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome        VARCHAR2(150)  NOT NULL,
    email       VARCHAR2(150)  NOT NULL UNIQUE,
    senha       VARCHAR2(255)  NOT NULL,
    role        VARCHAR2(30)   DEFAULT 'USER' NOT NULL,
    ativo       NUMBER(1)      DEFAULT 1 NOT NULL,
    criado_em   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Índices para performance
CREATE INDEX idx_licenca_empresa ON licenca_ambiental(empresa_id);
CREATE INDEX idx_licenca_validade ON licenca_ambiental(data_validade);
CREATE INDEX idx_emissao_empresa ON emissao_carbono(empresa_id);
CREATE INDEX idx_emissao_periodo ON emissao_carbono(periodo_ref);
CREATE INDEX idx_auditoria_empresa ON auditoria(empresa_id);
