package com.springboot.credit_application_service.infrastructure.rest.dto;

public record RiskEvaluationResponse(
        String document,
        int riskScore,
        String riskLevel,
        String details
) { }
