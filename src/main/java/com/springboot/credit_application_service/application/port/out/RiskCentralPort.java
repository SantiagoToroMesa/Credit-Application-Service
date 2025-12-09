package com.springboot.credit_application_service.application.port.out;

import com.springboot.credit_application_service.domain.model.RiskEvaluation;

import java.math.BigDecimal;

public interface RiskCentralPort {
    RiskEvaluation evaluateRisk(String documentNumber, BigDecimal amount, int term);
}
