package com.springboot.credit_application_service.application.port.in.Affiliate;

import com.springboot.credit_application_service.application.service.command.UpdateAffiliateCommand;
import com.springboot.credit_application_service.domain.model.Affiliate;

public interface UpdateAffiliateUseCase {
    Affiliate update(String documentNumber, UpdateAffiliateCommand command);
}
