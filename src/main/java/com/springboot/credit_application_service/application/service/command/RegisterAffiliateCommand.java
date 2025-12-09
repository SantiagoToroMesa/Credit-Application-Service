package com.springboot.credit_application_service.application.service.command;

import com.springboot.credit_application_service.domain.valueObjects.AffiliateStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterAffiliateCommand(
        String documentNumber,
        String name,
        BigDecimal salary,
        LocalDate affiliationDate,
        AffiliateStatus status
) {
}
