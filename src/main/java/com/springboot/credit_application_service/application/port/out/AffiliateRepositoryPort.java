package com.springboot.credit_application_service.application.port.out;

import com.springboot.credit_application_service.domain.model.Affiliate;

import java.util.Optional;

public interface AffiliateRepositoryPort {
    Affiliate save(Affiliate affiliate);
    Optional<Affiliate> findByDocument(String documentNumber);
    boolean exitsByDocument(String documentNumber);
}
