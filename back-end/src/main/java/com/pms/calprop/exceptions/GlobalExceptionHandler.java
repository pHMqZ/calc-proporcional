package com.pms.calprop.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pms.calprop.dto.StandardError;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        StandardError error = new StandardError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<StandardError> handleMissingHeader(MissingRequestHeaderException e,
            HttpServletRequest request) {
        String errorMesage = "X-Client-Id".equals(e.getHeaderName())
                ? "Sessão inválida ou expirada. Recarregue a página para continuar."
                : e.getMessage();

        StandardError err = new StandardError(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Sessão inválida ou expirada. Recarregue a página para continuar.",
                errorMesage,
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
