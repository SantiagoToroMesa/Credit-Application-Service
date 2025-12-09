package com.springboot.credit_application_service.application.service.Affiliate;

import com.springboot.credit_application_service.application.exception.ApplicationException;
import com.springboot.credit_application_service.application.port.in.Affiliate.RegisterAffiliatePort;
import com.springboot.credit_application_service.application.port.out.AffiliateRepositoryPort;
import com.springboot.credit_application_service.application.service.command.RegisterAffiliateCommand;
import com.springboot.credit_application_service.domain.model.Affiliate;
import com.springboot.credit_application_service.domain.valueObjects.AffiliateStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RegisterAffiliateUseCase implements RegisterAffiliatePort {

    private final AffiliateRepositoryPort affiliateRepo;

    @Override
    public Affiliate register(RegisterAffiliateCommand command) {

        if (affiliateRepo.exitsByDocument(command.documentNumber())) {
            throw new ApplicationException("Document already exists");
        }

        if (command.salary() == null || command.salary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApplicationException("Salary must be greater than 0");
        }

        var affiliate = new Affiliate(
                command.name(),
                command.documentNumber(),
                command.salary(),
                command.affiliationDate(),
                AffiliateStatus.ACTIVE
        );

        return affiliateRepo.save(affiliate);
    }
}

