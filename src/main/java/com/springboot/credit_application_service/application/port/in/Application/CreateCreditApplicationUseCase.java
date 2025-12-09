package com.springboot.credit_application_service.application.port.in.Application;

import com.springboot.credit_application_service.application.service.command.CreateCreditApplicationCommand;
import com.springboot.credit_application_service.domain.model.CreditApplication;

public interface CreateCreditApplicationUseCase {
    CreditApplication create(CreateCreditApplicationCommand command);
}
