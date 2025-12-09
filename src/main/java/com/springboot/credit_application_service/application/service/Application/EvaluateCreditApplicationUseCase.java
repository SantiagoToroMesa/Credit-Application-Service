package com.springboot.credit_application_service.application.service.Application;

import com.springboot.credit_application_service.application.exception.ApplicationException;
import com.springboot.credit_application_service.application.port.in.Application.EvaluateCreditApplicationPort;
import com.springboot.credit_application_service.application.port.out.CreditApplicationRepositoryPort;
import com.springboot.credit_application_service.application.port.out.RiskCentralPort;
import com.springboot.credit_application_service.application.service.command.EvaluateCreditApplicationCommand;
import com.springboot.credit_application_service.domain.model.CreditApplication;
import com.springboot.credit_application_service.domain.model.RiskEvaluation;
import com.springboot.credit_application_service.domain.valueObjects.ApplicationStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluateCreditApplicationUseCase implements EvaluateCreditApplicationPort {

    private final CreditApplicationRepositoryPort creditRepo;
    private final RiskCentralPort riskCentralPort;

    @Transactional
    @Override
    public CreditApplication evaluate(EvaluateCreditApplicationCommand command) {


        var application = creditRepo.findById(command.applicationId())
                .orElseThrow(() -> new ApplicationException("Credit application not found"));


        RiskEvaluation evaluation = riskCentralPort.evaluateRisk(
                application.affiliate().documentNumber(),
                application.amount(),
                application.termMonths()
        );


        ApplicationStatus status =
                evaluation.riskScore() >= 600 ? ApplicationStatus.APPROVED : ApplicationStatus.REJECTED;

        var updated = application.withEvaluation(evaluation).withStatus(status);


        return creditRepo.save(updated);
    }
}
