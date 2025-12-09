package com.springboot.credit_application_service.domain.model;

import com.springboot.credit_application_service.domain.valueObjects.AffiliateStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public record Affiliate(
    String name,
    String documentNumber,
    BigDecimal salary,
    LocalDate affiliationDate,
    AffiliateStatus status
) {
}
