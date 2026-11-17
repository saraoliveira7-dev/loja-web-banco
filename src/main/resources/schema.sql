-- Referência do esquema (o mesmo SQL é executado automaticamente
-- na inicialização da aplicação por InicializadorBanco).
CREATE TABLE IF NOT EXISTS usuario (
    id    INTEGER      PRIMARY KEY,
    nome  VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(64)  NOT NULL          -- hash SHA-256 em hexadecimal
);

CREATE TABLE IF NOT EXISTS produto (
    id        INTEGER       PRIMARY KEY,
    nome      VARCHAR(100)  NOT NULL,
    descricao VARCHAR(255)  NOT NULL,
    preco     NUMERIC(10,2) NOT NULL,
    icone     VARCHAR(20)
);
