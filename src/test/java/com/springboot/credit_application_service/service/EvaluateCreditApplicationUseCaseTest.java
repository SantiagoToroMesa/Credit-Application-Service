package com.springboot.credit_application_service.service;

import com.springboot.credit_application_service.application.exception.ApplicationException;
import com.springboot.credit_application_service.application.port.out.CreditApplicationRepositoryPort;
import com.springboot.credit_application_service.application.port.out.RiskCentralPort;
import com.springboot.credit_application_service.application.service.Application.EvaluateCreditApplicationUseCase;
import com.springboot.credit_application_service.application.service.command.EvaluateCreditApplicationCommand;
import com.springboot.credit_application_service.domain.model.Affiliate;
import com.springboot.credit_application_service.domain.model.CreditApplication;
import com.springboot.credit_application_service.domain.model.RiskEvaluation;
import com.springboot.credit_application_service.domain.valueObjects.AffiliateStatus;
import com.springboot.credit_application_service.domain.valueObjects.ApplicationStatus;
import com.springboot.credit_application_service.domain.valueObjects.RiskLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt; // IMPORTADO PARA EVITAR NPE
import static org.mockito.ArgumentMatchers.anyString; // Usamos anyString para mayor claridad
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluateCreditApplicationUseCaseTest {

    @Mock
    private CreditApplicationRepositoryPort creditRepo;

    @Mock
    private RiskCentralPort riskCentralPort;

    @InjectMocks
    private EvaluateCreditApplicationUseCase useCase;

    private CreditApplication pendingApplication;
    private EvaluateCreditApplicationCommand command;
    private Affiliate mockAffiliate;
    // El ID es un String, lo definimos como constante de String
    private final String APPLICATION_ID_STRING = "1";

    @BeforeEach
    void setUp() {
        // 1. Crear el Affiliate (Record inmutable)
        mockAffiliate = new Affiliate(
                "Juan Perez",
                "123456",
                new BigDecimal("50000"),
                LocalDate.now(),
                AffiliateStatus.ACTIVE
        );

        // 2. Crear la CreditApplication (Record inmutable)
        pendingApplication = new CreditApplication(
                APPLICATION_ID_STRING, // ID de la aplicación como String
                mockAffiliate,
                new BigDecimal("10000"),
                12,
                new BigDecimal("0.05"),
                LocalDate.now(),
                ApplicationStatus.PENDING,
                null
        );

        // 3. Comando de Evaluación
        command = new EvaluateCreditApplicationCommand(pendingApplication.id());
    }

    // --- Casos de Prueba ---

    @Test
    void evaluate_ShouldApproveApplication_WhenRiskScoreIsHigh() {
        // Arrange
        // Usamos String para el ID de la aplicación
        when(creditRepo.findById(APPLICATION_ID_STRING)).thenReturn(Optional.of(pendingApplication));

        RiskEvaluation highRiskEvaluation = new RiskEvaluation(650, RiskLevel.LOW, "Score aceptable.");

        // MOCKITO FIX: Usamos anyInt() para el tercer argumento (Integer/int)
        when(riskCentralPort.evaluateRisk(
                anyString(), // DocumentNumber (String)
                any(BigDecimal.class), // Amount (BigDecimal)
                anyInt() // TermMonths (int/Integer)
        )).thenReturn(highRiskEvaluation);

        when(creditRepo.save(any(CreditApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CreditApplication result = useCase.evaluate(command);

        // Assert
        assertEquals(ApplicationStatus.APPROVED, result.status(), "La aplicación debería ser APROBADA.");
        assertNotNull(result.evaluation(), "Debería tener la evaluación adjunta.");
        assertEquals(650, result.evaluation().riskScore());

        // Verify: Asegurar que se llamó a la evaluación y se guardó el resultado
        verify(riskCentralPort).evaluateRisk(anyString(), any(BigDecimal.class), anyInt());
        verify(creditRepo).save(any(CreditApplication.class));
    }

    @Test
    void evaluate_ShouldRejectApplication_WhenRiskScoreIsLow() {
        // Arrange
        when(creditRepo.findById(APPLICATION_ID_STRING)).thenReturn(Optional.of(pendingApplication));

        RiskEvaluation lowRiskEvaluation = new RiskEvaluation(550, RiskLevel.HIGH, "Score bajo riesgo.");
        when(riskCentralPort.evaluateRisk(anyString(), any(BigDecimal.class), anyInt())).thenReturn(lowRiskEvaluation);

        when(creditRepo.save(any(CreditApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CreditApplication result = useCase.evaluate(command);

        // Assert
        assertEquals(ApplicationStatus.REJECTED, result.status(), "La aplicación debería ser RECHAZADA.");
        assertEquals(550, result.evaluation().riskScore());
    }

    @Test
    void evaluate_ShouldThrowException_WhenApplicationNotFound() {
        // Arrange: Simular que la solicitud no existe, usando ID de String
        when(creditRepo.findById("99")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ApplicationException.class, () -> {
            useCase.evaluate(new EvaluateCreditApplicationCommand("99"));
        }, "Debería lanzar ApplicationException si la solicitud no se encuentra.");
    }
}