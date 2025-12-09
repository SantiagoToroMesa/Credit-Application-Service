package com.springboot.credit_application_service.application.port.out;

import com.springboot.credit_application_service.domain.model.CreditApplication;

import java.util.List;
import java.util.Optional;

public interface CreditApplicationRepositoryPort {
    CreditApplication save(CreditApplication application);
    Optional<CreditApplication> findById(String id);
    List<CreditApplication> findByAffiliateDocument(String documentNumber);
}
