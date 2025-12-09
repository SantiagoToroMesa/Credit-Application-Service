package com.springboot.credit_application_service.infrastructure.persistence.entity;

import com.springboot.credit_application_service.domain.valueObjects.AffiliateStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "affiliates")
@Data
public class AffiliateEntity {

    @Id
    @Column(name = "document_number")
    private String documentNumber;

    private String name;

    private BigDecimal salary;

    private LocalDate affiliationDate;

    @Enumerated(EnumType.STRING)
    private AffiliateStatus status;
}
