package com.springboot.credit_application_service.domain.model;

import com.springboot.credit_application_service.domain.valueObjects.RiskLevel;

public record RiskEvaluation(
        int score,
        RiskLevel riskLevel,
        String detail
) {
}
