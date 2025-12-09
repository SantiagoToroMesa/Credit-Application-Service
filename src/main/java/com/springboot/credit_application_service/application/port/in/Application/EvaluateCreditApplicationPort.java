package com.springboot.credit_application_service.application.port.in.Application;

import com.springboot.credit_application_service.application.service.command.EvaluateCreditApplicationCommand;
import com.springboot.credit_application_service.domain.model.CreditApplication;

public interface EvaluateCreditApplicationPort {
    CreditApplication evaluate(EvaluateCreditApplicationCommand command);
}
