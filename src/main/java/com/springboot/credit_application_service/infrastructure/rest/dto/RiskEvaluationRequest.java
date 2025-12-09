package com.springboot.credit_application_service.infrastructure.rest.dto;

import java.math.BigDecimal;

public record RiskEvaluationRequest(
        String document,
        BigDecimal amount,
        int term
) {}
