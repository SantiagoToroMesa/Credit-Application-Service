package com.springboot.credit_application_service.infrastructure.persistence.entity;

import com.springboot.credit_application_service.domain.valueObjects.RiskLevel;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "risk_evaluations")
@Data
public class RiskEvaluationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    private String detail;
}
