package com.springboot.credit_application_service.infrastructure.controller;

import com.springboot.credit_application_service.application.port.in.Application.CreateCreditApplicationPort;
import com.springboot.credit_application_service.application.port.in.Application.EvaluateCreditApplicationPort;
import com.springboot.credit_application_service.application.port.in.Application.GetCreditApplicationsPort;
import com.springboot.credit_application_service.application.service.command.CreateCreditApplicationCommand;
import com.springboot.credit_application_service.application.service.command.EvaluateCreditApplicationCommand;
import com.springboot.credit_application_service.domain.model.CreditApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-applications")
@RequiredArgsConstructor
@Slf4j
public class CreditApplicationController {
    private final EvaluateCreditApplicationPort evaluatePort;
    private final CreateCreditApplicationPort createCreditPort;
    private final GetCreditApplicationsPort getPort;

    @PostMapping
    public CreditApplication create(@RequestBody CreateCreditApplicationCommand command) {
        return createCreditPort.create(command);
    }

    @PutMapping("/{id}/evaluate")
    public CreditApplication evaluate(@PathVariable String id) {

        var command = new EvaluateCreditApplicationCommand(id);

        return evaluatePort.evaluate(command);
    }


    @GetMapping("/affiliate/{document}")
    public List<CreditApplication> getByAffiliateDocument(@PathVariable String document) {
        return getPort.findByAffiliate(document);
    }


}
