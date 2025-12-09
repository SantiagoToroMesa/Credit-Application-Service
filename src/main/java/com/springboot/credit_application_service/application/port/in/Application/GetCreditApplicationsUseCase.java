package com.springboot.credit_application_service.application.port.in.Application;

import com.springboot.credit_application_service.domain.model.CreditApplication;

import java.util.List;

public interface GetCreditApplicationsUseCase {
    List<CreditApplication> findByAffiliate(String documentNumber);
}
