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

    public static CreditApplication create(
            Affiliate affiliate,
            BigDecimal amount,
            int termMonths,
            BigDecimal interestRate,
            RiskEvaluation evaluation
    ) {
        return new CreditApplication(
                null,
                affiliate,
                amount,
                termMonths,
                interestRate,
                LocalDate.now(),
                ApplicationStatus.PENDING,
                evaluation
        );
    }

    public CreditApplication withEvaluation(RiskEvaluation updated) {
        return new CreditApplication(
                this.id,
                this.affiliate,
                this.amount,
                this.termMonths,
                this.interestRate,
                this.applicationDate,
                this.status,
                updated
        );
    }

    public CreditApplication withStatus(ApplicationStatus newStatus) {
        return new CreditApplication(
                this.id,
                this.affiliate,
                this.amount,
                this.termMonths,
                this.interestRate,
                this.applicationDate,
                newStatus,
                this.evaluation
        );
    }
}
