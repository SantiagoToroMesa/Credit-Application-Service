package com.springboot.credit_application_service;

import com.springboot.credit_application_service.AbstractIntegrationTest;
// **QUITAMOS LA IMPORTACIÓN DEL PORT (puerto del dominio)**
// import com.springboot.credit_application_service.application.port.out.CreditApplicationRepositoryPort;
import com.springboot.credit_application_service.domain.valueObjects.ApplicationStatus;

import com.springboot.credit_application_service.infrastructure.persistence.entity.AffiliateEntity;
import com.springboot.credit_application_service.infrastructure.persistence.entity.CreditApplicationEntity;
// **IMPORTAMOS EL REPOSITORIO REAL DE SPRING DATA JPA PARA CreditApplication**
import com.springboot.credit_application_service.infrastructure.persistence.repository.CreditApplicationSpringRepository;
import com.springboot.credit_application_service.infrastructure.persistence.repository.AffiliateSpringRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CreditApplicationIntegrationTest extends AbstractIntegrationTest {

    // **CORRECCIÓN: Inyectamos el REPOSITORIO REAL de Spring Data JPA**
    @Autowired
    private CreditApplicationSpringRepository creditApplicationSpringRepository;

    @Autowired
    private AffiliateSpringRepository affiliateRepository;

    @Test
    void saveAndFindApplication_ShouldSucceedAndLoadAffiliate() {
        // --- ARRANGE ---
        AffiliateEntity affiliate = new AffiliateEntity();
        affiliate.setName("Test Integracion JPA");
        affiliate.setDocumentNumber("987654321");
        affiliate.setSalary(new BigDecimal("60000"));
        AffiliateEntity savedAffiliate = affiliateRepository.save(affiliate);

        CreditApplicationEntity newApplication = new CreditApplicationEntity();
        newApplication.setAffiliate(savedAffiliate);
        newApplication.setAmount(new BigDecimal("15000"));
        newApplication.setTermMonths(24);
        newApplication.setApplicationDate(LocalDate.now());
        newApplication.setStatus(ApplicationStatus.PENDING);

        // --- ACT ---
        // **CORRECCIÓN Línea 57**: Usamos el repositorio Spring Data JPA
        CreditApplicationEntity savedApplication = creditApplicationSpringRepository.save(newApplication);

        // --- ASSERT (Initial Check) ---
        assertNotNull(savedApplication.getId(), "El ID de la aplicación debe ser generado por la DB.");

        // --- ACT (Read) ---
        // **CORRECCIÓN Línea 65**: Usamos el repositorio Spring Data JPA
        CreditApplicationEntity foundApplication = creditApplicationSpringRepository
                .findById(savedApplication.getId())
                .orElseThrow(() -> new AssertionError("Aplicación no encontrada en la DB."));

        // --- ASSERT (Final Check) ---
        assertEquals("987654321", foundApplication.getAffiliate().getDocumentNumber());
        assertEquals(ApplicationStatus.PENDING, foundApplication.getStatus());
        assertEquals(new BigDecimal("15000"), foundApplication.getAmount());
    }
}