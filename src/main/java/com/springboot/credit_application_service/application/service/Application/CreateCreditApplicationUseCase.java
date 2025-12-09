package com.springboot.credit_application_service.application.service.Application;

import com.springboot.credit_application_service.application.exception.ApplicationException;
import com.springboot.credit_application_service.application.port.in.Application.CreateCreditApplicationPort;
import com.springboot.credit_application_service.application.port.out.AffiliateRepositoryPort;
import com.springboot.credit_application_service.application.port.out.CreditApplicationRepositoryPort;
import com.springboot.credit_application_service.application.port.out.RiskCentralPort;
import com.springboot.credit_application_service.application.service.command.CreateCreditApplicationCommand;
import com.springboot.credit_application_service.domain.model.CreditApplication;
import com.springboot.credit_application_service.domain.valueObjects.AffiliateStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCreditApplicationUseCase implements CreateCreditApplicationPort {

    private final CreditApplicationRepositoryPort creditRepo;
    private final AffiliateRepositoryPort affiliateRepo;
    private final RiskCentralPort riskCentralPort;

    public CreditApplication create(CreateCreditApplicationCommand command) {

        var affiliate = affiliateRepo.findByDocument(command.affiliateDocumentNumber())
                .orElseThrow(() -> new ApplicationException("Affiliate not found"));

        if (affiliate.status() != AffiliateStatus.ACTIVE) {
            throw new ApplicationException("Affiliate must be ACTIVE to request a credit.");
        }

        // Evaluar riesgo
        var evaluation = riskCentralPort.evaluateRisk(
                affiliate.documentNumber(),
                command.amount(),
                command.termMonths()
        );

        var application = CreditApplication.create(
                affiliate,
                command.amount(),
                command.termMonths(),
                command.interestRate(),
                evaluation
        );

        return creditRepo.save(application);
    }

}



