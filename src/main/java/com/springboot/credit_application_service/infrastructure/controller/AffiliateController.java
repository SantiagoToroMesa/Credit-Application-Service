package com.springboot.credit_application_service.infrastructure.controller;

import com.springboot.credit_application_service.application.port.in.Affiliate.RegisterAffiliatePort;
import com.springboot.credit_application_service.application.port.in.Affiliate.UpdateAffiliatePort;
import com.springboot.credit_application_service.application.service.command.RegisterAffiliateCommand;
import com.springboot.credit_application_service.application.service.command.UpdateAffiliateCommand;
import com.springboot.credit_application_service.domain.model.Affiliate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/affiliates")
@RequiredArgsConstructor
@Slf4j
public class AffiliateController {

    private final RegisterAffiliatePort registerPort;
    private final UpdateAffiliatePort updatePort;

    @PostMapping
    public Affiliate register(@RequestBody RegisterAffiliateCommand command) {
        return registerPort.register(command);
    }

    @PutMapping("/{documentNumber}")
    public Affiliate update(
            @PathVariable String documentNumber,
            @RequestBody UpdateAffiliateCommand request) {

        var command = new UpdateAffiliateCommand(
                documentNumber,
                request.name(),
                request.salary()
        );

        return updatePort.update(command);
    }
}
