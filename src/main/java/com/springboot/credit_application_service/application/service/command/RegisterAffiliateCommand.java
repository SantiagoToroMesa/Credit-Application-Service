package com.springboot.credit_application_service.application.service.command;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterAffiliateCommand(
        String documentNumber,
        String name,
        BigDecimal salary,
        LocalDate affiliationDate
) {
}
