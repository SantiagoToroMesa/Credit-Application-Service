package com.springboot.credit_application_service.application.service.Affiliate;

import com.springboot.credit_application_service.application.exception.ApplicationException;
import com.springboot.credit_application_service.application.port.in.Affiliate.UpdateAffiliatePort;
import com.springboot.credit_application_service.application.port.out.AffiliateRepositoryPort;
import com.springboot.credit_application_service.application.service.command.UpdateAffiliateCommand;
import com.springboot.credit_application_service.domain.model.Affiliate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateAffiliateUseCase implements UpdateAffiliatePort {

    private final AffiliateRepositoryPort affiliateRepo;

    @Override
    public Affiliate update(UpdateAffiliateCommand command) {

        var affiliate = affiliateRepo.findByDocument(command.documentNumber())
                .orElseThrow(() -> new ApplicationException("Affiliate not found"));

        var updated = affiliate.update(
                command.name(),
                command.salary()
        );

        return affiliateRepo.save(updated);
    }
}
