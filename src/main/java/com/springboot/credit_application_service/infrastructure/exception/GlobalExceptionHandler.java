package com.springboot.credit_application_service.infrastructure.exception;


import com.springboot.credit_application_service.application.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.MDC;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_URI = "https://coopcredit.com/errors/";
    private static final String TRACE_ID_KEY = "traceId";

    /**
     * Retrieves the correlation ID from the MDC (set by the Interceptor).
     * Generates a fallback ID if the MDC is empty.
     */
    private String getCorrelationId() {
        String traceId = MDC.get(TRACE_ID_KEY);
        return traceId != null ? traceId : "N/A-" + UUID.randomUUID().toString();
    }

    // --- Spring Security Handlers (403 Access Denied) ---

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        String traceId = getCorrelationId();
        // Log a nivel WARN, pero sin traza de pila (es un evento esperado)
        log.warn("Access Denied (403) - traceId={}: {}", traceId, ex.getMessage());

        // Status 403 Forbidden
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Access to this resource is denied based on user roles or permissions.");
        pd.setTitle("Access Denied (Forbidden)");
        pd.setType(URI.create(BASE_URI + "access-denied"));
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty(TRACE_ID_KEY, traceId);
        return pd;
    }

    // --- Spring Security Handlers (401 Authentication Failed) ---

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationFailed(AuthenticationException ex) {
        String traceId = getCorrelationId();
        log.warn("Authentication Failed (401) - traceId={}: {}", traceId, ex.getMessage());

        // Status 401 Unauthorized
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Authentication failed: Invalid credentials or token.");
        pd.setTitle("Authentication Failed (Unauthorized)");
        pd.setType(URI.create(BASE_URI + "authentication-failed"));
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty(TRACE_ID_KEY, traceId);
        return pd;
    }

    // --- 1. Domain and Application Exceptions ---

    @ExceptionHandler(ApplicationException.class)
    public ProblemDetail handleApplicationException(ApplicationException ex, WebRequest request) {
        String traceId = getCorrelationId();
        // ESTA LÍNEA ES CRUCIAL PARA IMPRIMIR EL ERROR COMPLETO
        log.error("Application Business Rule Failed - traceId={}", traceId, ex);

        // Status 422 Unprocessable Entity (Mejor para reglas de negocio)
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        pd.setTitle("Application Business Rule Failed");
        pd.setType(URI.create(BASE_URI + "application-rule"));
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty(TRACE_ID_KEY, traceId);
        return pd;
    }

    // --- 2. Framework/Validation Exceptions (Bean Validation) ---

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        String traceId = getCorrelationId();
        // Se loguea como error para revisar fallos en DTOs
        log.error("Validation error (Request Body) - traceId={}", traceId, ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle("Validation Failed");
        pd.setType(URI.create(BASE_URI + "invalid-arguments"));

        // Collect all field errors into a Map for the ProblemDetail body
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existing, replacement) -> existing
                ));

        pd.setDetail("The request body contains invalid data fields.");
        pd.setProperty("validationErrors", errors);
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty(TRACE_ID_KEY, traceId);

        return pd;
    }

    // Cambiada la importación para usar la de Hibernate Validator/JPA
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintValidation(ConstraintViolationException ex, WebRequest request) {
        String traceId = getCorrelationId();
        log.error("Constraint Violation (Path/Param) - traceId={}", traceId, ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle("Constraint Violation");
        pd.setType(URI.create(BASE_URI + "constraint-violation"));

        // Nota: La forma de acceder a las violaciones puede variar ligeramente entre el ConstraintViolationException
        // de Spring Validation y el de Hibernate/JPA.

        pd.setDetail("One or more request parameters or variables failed validation.");
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty(TRACE_ID_KEY, traceId);

        return pd;
    }


    // --- 3. Common Persistence/API Exceptions ---

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        String traceId = getCorrelationId();
        log.warn("Resource Not Found (404) - traceId={}", traceId, ex);
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        pd.setTitle("Resource Not Found");
        pd.setType(URI.create(BASE_URI + "resource-not-found"));
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty(TRACE_ID_KEY, traceId);
        return pd;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String traceId = getCorrelationId();
        // ESTA LÍNEA ES CRUCIAL
        log.error("Data Conflict (409) - traceId={}", traceId, ex);
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle("Data Conflict");
        pd.setType(URI.create(BASE_URI + "data-integrity-conflict"));

        String rootCause = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "Database integrity constraint failed.";

        pd.setDetail("A database constraint violation occurred (e.g., duplicate key, foreign key failure).");
        pd.setProperty("rootCause", rootCause);
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty(TRACE_ID_KEY, traceId);
        return pd;
    }

    // --- 4. Generic Fallback ---

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, WebRequest request) {
        String traceId = getCorrelationId();
        // ESTA LÍNEA ES CRUCIAL
        log.error("Unexpected Internal Error - traceId={}", traceId, ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, "An unexpected internal error occurred. Please contact support.");
        pd.setTitle("Internal Server Error");
        pd.setType(URI.create(BASE_URI + "generic-internal-error"));

        pd.setProperty("exceptionType", ex.getClass().getSimpleName());
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty(TRACE_ID_KEY, traceId);
        return pd;
    }
}