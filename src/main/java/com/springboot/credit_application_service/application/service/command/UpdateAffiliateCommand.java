package com.springboot.credit_application_service.application.service.command;

import java.math.BigDecimal;

public record UpdateAffiliateCommand(
        String documentNumber,
        String name,
        BigDecimal salary
) { }
