package com.springboot.credit_application_service.infrastructure.persistence.repository;

import com.springboot.credit_application_service.infrastructure.persistence.entity.CreditApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditApplicationSpringRepository extends JpaRepository<CreditApplicationEntity, Long> {
}
