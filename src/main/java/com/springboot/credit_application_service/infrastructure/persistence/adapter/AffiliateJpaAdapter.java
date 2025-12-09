package com.springboot.credit_application_service.infrastructure.persistence.adapter;

import com.springboot.credit_application_service.application.port.out.AffiliateRepositoryPort;
import com.springboot.credit_application_service.domain.model.Affiliate;
import com.springboot.credit_application_service.infrastructure.mapper.AffiliateMapper;
import com.springboot.credit_application_service.infrastructure.persistence.entity.AffiliateEntity;
import com.springboot.credit_application_service.infrastructure.persistence.repository.AffiliateSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AffiliateJpaAdapter implements AffiliateRepositoryPort {

    private final AffiliateSpringRepository repository;
    private final AffiliateMapper mapper;

    @Override
    public Affiliate save(Affiliate affiliate) {
        AffiliateEntity entity = mapper.toEntity(affiliate);
        AffiliateEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Affiliate> findByDocument(String documentNumber) {
        return repository.findById(documentNumber)
                .map(mapper::toDomain);
    }

    @Override
    public boolean exitsByDocument(String documentNumber) {
        return repository.existsById(documentNumber);

    }
}

