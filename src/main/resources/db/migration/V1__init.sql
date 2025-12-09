-- V1__schema.sql

-- Tabla: affiliates (Afiliado)
CREATE TABLE affiliates (
    document_number VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    salary DECIMAL(19, 2),
    affiliation_date DATE,
    status VARCHAR(50) NOT NULL
);

-- Tabla: risk_evaluations (Evaluación de Riesgo)
-- Nota: Usaremos esta tabla como la dueña de la relación 1-1.
CREATE TABLE risk_evaluations (
    id BIGINT PRIMARY KEY, -- Usará el mismo ID que la solicitud
    score INT,
    risk_level VARCHAR(50),
    detail TEXT
);

-- Tabla: credit_applications (Solicitud de Crédito)
CREATE TABLE credit_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    affiliate_document VARCHAR(255) NOT NULL, -- Clave foránea al afiliado
    amount DECIMAL(19, 2),
    term_months INT,
    interest_rate DECIMAL(5, 2),
    application_date DATE,
    status VARCHAR(50) NOT NULL
    -- La clave foránea a risk_evaluations NO va aquí, va en V2.
);