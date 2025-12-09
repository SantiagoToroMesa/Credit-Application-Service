package com.springboot.credit_application_service.infrastructure.persistence.entity;

import com.springboot.credit_application_service.domain.model.RiskEvaluation;
import com.springboot.credit_application_service.domain.valueObjects.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "credit_applications")
@Data
public class CreditApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_document")
    private AffiliateEntity affiliate;

    private BigDecimal amount;

    private int termMonths;

    private BigDecimal interestRate;

    private LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "evaluation_id")
    private RiskEvaluationEntity evaluation;
}
