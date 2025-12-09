package com.springboot.credit_application_service.infrastructure.persistence.adapter;

import com.springboot.credit_application_service.application.port.out.CreditApplicationRepositoryPort;
import com.springboot.credit_application_service.domain.model.CreditApplication;
import com.springboot.credit_application_service.infrastructure.mapper.CreditApplicationMapper;
import com.springboot.credit_application_service.infrastructure.persistence.entity.CreditApplicationEntity;
import com.springboot.credit_application_service.infrastructure.persistence.repository.CreditApplicationSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreditApplicationJpaAdapter implements CreditApplicationRepositoryPort {

    private final CreditApplicationSpringRepository repository;
    private final CreditApplicationMapper mapper;

    @Override
    public CreditApplication save(CreditApplication app) {
        CreditApplicationEntity entity = mapper.toEntity(app);
        CreditApplicationEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<CreditApplication> findByAffiliateDocument(String documentNumber) {
        return repository.findByAffiliate_DocumentNumber(documentNumber)
                .stream()
                // El lambda ahora es compatible, ya que 'entity' es un CreditApplicationEntity
                .map(entity -> mapper.toDomain(entity))
                .toList();
    }

    @Override
    public Optional<CreditApplication> findById(String id) {
        // 1. Asumiendo que el ID de la entidad es Long (según tu CreditApplicationEntity)
        // Debes convertir el String 'id' a Long.
        Long entityId;
        try {
            entityId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            // Manejar error si el ID no es un número (lanzar una excepción de tipo Bad Request, por ejemplo)
            return Optional.empty(); // O lanzar una excepción adecuada
        }

        // 2. Usar el método findById del JpaRepository
        return repository.findById(entityId)
                // 3. Mapear la Entidad al Modelo de Dominio si se encuentra
                .map(mapper::toDomain);
    }
}