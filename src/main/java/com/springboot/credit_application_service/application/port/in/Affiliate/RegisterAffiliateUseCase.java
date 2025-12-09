package com.springboot.credit_application_service.application.port.in.Affiliate;

import com.springboot.credit_application_service.application.service.command.RegisterAffiliateCommand;
import com.springboot.credit_application_service.domain.model.Affiliate;

public interface RegisterAffiliateUseCase {
    Affiliate register(RegisterAffiliateCommand command);
}
