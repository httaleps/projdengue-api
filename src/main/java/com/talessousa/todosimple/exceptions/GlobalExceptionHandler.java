package com.talessousa.todosimple.exceptions;

import com.talessousa.todosimple.services.exceptions.DataBindingViolationException;
import com.talessousa.todosimple.services.exceptions.ObjectNotFoundException;

import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;


@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Value("${server.error.include-exception:false}")
    private boolean printStackTrace;


    // Handler para @Valid e @Validated (MethodArgumentNotValidException)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {


        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details."
        );


        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }


        log.warn("Erro de validação: {}", ex.getMessage());
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }


    // Handler para ConstraintViolationException (validação em nível de entidade)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request) {
        log.error("Erro de validação de restrição: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex, "Validation failed", HttpStatus.UNPROCESSABLE_ENTITY, request);
    }


    // Handler para ObjectNotFoundException (404)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException ex,
            WebRequest request) {
        log.warn("Objeto não encontrado: {}", ex.getMessage());
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }


    // Handler para DataBindingViolationException (409)
    @ExceptionHandler(DataBindingViolationException.class)
    public ResponseEntity<Object> handleDataBindingViolationException(
            DataBindingViolationException ex,
            WebRequest request) {
        log.warn("Violação de ligação de dados: {}", ex.getMessage());
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.CONFLICT, request);
    }


    // Handler para DataIntegrityViolationException (409)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            WebRequest request) {
        String message = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();
        log.error("Violação de integridade de dados: {}", message, ex);
        return buildErrorResponse(ex, message != null ? message : "Data integrity violation", HttpStatus.CONFLICT, request);
    }


    // Handler para falhas de autenticação (ex: login inválido)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex,
            WebRequest request) {
        log.warn("Falha na autenticação: {}", ex.getMessage());
        return buildErrorResponse(ex, "Credenciais inválidas", HttpStatus.UNAUTHORIZED, request);
    }


    // Handler genérico para exceções não tratadas (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception ex,
            WebRequest request) {
        String message = "An unexpected error occurred";
        log.error(message, ex);
        return buildErrorResponse(ex, message, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    // Método utilitário para construir respostas de erro
    private ResponseEntity<Object> buildErrorResponse(
            Exception ex,
            String message,
            HttpStatus status,
            WebRequest request) {


        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);


        if (printStackTrace) {
            errorResponse.setStackTrace(getStackTraceAsString(ex));
        }


        return ResponseEntity.status(status).body(errorResponse);
    }


    // Converte o stack trace em String sem depender do Apache Commons
    private String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
