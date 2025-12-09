package com.springboot.credit_application_service.domain.model;

import com.springboot.credit_application_service.domain.valueObjects.ApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditApplication(
        String id,
        Affiliate affiliate,
        BigDecimal amount,
        int termMonths,
        BigDecimal interestRate,
        LocalDate applicationDate,
        ApplicationStatus status,
        RiskEvaluation evaluation
) {

}
