-- V2__relaciones.sql

-- 1. Relación 1-N: CreditApplication a Affiliate
-- Cada solicitud pertenece a un afiliado.
ALTER TABLE credit_applications
ADD CONSTRAINT fk_application_affiliate
FOREIGN KEY (affiliate_document)
REFERENCES affiliates(document_number);


-- 2. Relación 1-1: CreditApplication a RiskEvaluation (Clave Compartida)
-- El ID de la evaluación de riesgo es la clave primaria compartida del ID de la solicitud.
ALTER TABLE risk_evaluations
ADD CONSTRAINT fk_evaluation_application
FOREIGN KEY (id)
REFERENCES credit_applications(id);