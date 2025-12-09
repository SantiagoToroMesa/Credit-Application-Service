package com.springboot.credit_application_service.infrastructure.persistence.repository;

import com.springboot.credit_application_service.infrastructure.persistence.entity.CreditApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Importa List

public interface CreditApplicationSpringRepository extends JpaRepository<CreditApplicationEntity, Long> {

    // 💡 SOLUCIÓN: Usar la convención de navegación y retornar la entidad correcta (List<CreditApplicationEntity>)
    List<CreditApplicationEntity> findByAffiliate_DocumentNumber(String documentNumber);
}