package com.springboot.credit_application_service.application.service.command;

import java.math.BigDecimal;

public record CreateCreditApplicationCommand(
        String affiliateDocumentNumber,
        BigDecimal amount,
        int termMonths,
        BigDecimal interestRate
) {
}
