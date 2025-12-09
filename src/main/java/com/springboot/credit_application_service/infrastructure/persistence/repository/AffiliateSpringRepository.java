package com.springboot.credit_application_service.infrastructure.persistence.repository;

import com.springboot.credit_application_service.infrastructure.persistence.entity.AffiliateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AffiliateSpringRepository extends JpaRepository<AffiliateEntity, String> {

}
