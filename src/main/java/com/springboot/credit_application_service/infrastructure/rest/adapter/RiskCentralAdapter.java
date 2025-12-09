package com.springboot.credit_application_service.infrastructure.rest.adapter;

import com.springboot.credit_application_service.application.port.out.RiskCentralPort;
import com.springboot.credit_application_service.domain.model.RiskEvaluation;
import com.springboot.credit_application_service.domain.valueObjects.RiskLevel;
import com.springboot.credit_application_service.infrastructure.rest.dto.RiskEvaluationRequest;
import com.springboot.credit_application_service.infrastructure.rest.dto.RiskEvaluationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RiskCentralAdapter implements RiskCentralPort {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String URL = "http://localhost:8081/risk-evaluation";

    @Override
    public RiskEvaluation evaluateRisk(String document, BigDecimal amount, int term) {

        // Crear request para el microservicio externo
        var request = new RiskEvaluationRequest(document, amount, term);

        var response = restTemplate.postForObject(
                URL,
                request,
                RiskEvaluationResponse.class
        );

        return new RiskEvaluation(
                response.riskScore(),
                RiskLevel.valueOf(response.riskLevel().toUpperCase()),
                response.details()
        );
    }
}

