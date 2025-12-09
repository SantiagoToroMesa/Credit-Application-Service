package com.springboot.credit_application_service.application.service.Application;

import com.springboot.credit_application_service.application.exception.ApplicationException;
import com.springboot.credit_application_service.application.port.in.Application.GetCreditApplicationsPort;
import com.springboot.credit_application_service.application.port.out.AffiliateRepositoryPort;
import com.springboot.credit_application_service.application.port.out.CreditApplicationRepositoryPort;
import com.springboot.credit_application_service.domain.model.CreditApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCreditApplicationsUseCase implements GetCreditApplicationsPort {
    private final AffiliateRepositoryPort affiliateRepo;
    private final CreditApplicationRepositoryPort creditRepo;


    @Override
    public List<CreditApplication> findByAffiliate(String documentNumber) {
        var affiliate = affiliateRepo.findByDocument(documentNumber)
                .orElseThrow(() -> new ApplicationException("Affiliate not found"));

        return creditRepo.findByAffiliateDocument(affiliate.documentNumber());
    }
}
