package com.mercadolibre.desafio.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ApiError> handleApiException(ApiException e) {
        Integer statusCode = e.getStatusCode();
        ApiError apiError = new ApiError(e.getCode(), e.getDescription(), statusCode);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
