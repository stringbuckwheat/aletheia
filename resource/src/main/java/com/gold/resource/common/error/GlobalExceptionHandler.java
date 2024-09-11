package com.gold.resource.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldError() != null ?
                e.getBindingResult().getFieldError().getDefaultMessage()
                : "입력 정보를 확인해주세요";

        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), message);

        return new ErrorResponse(message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String message = e.getParameterName() + "은/는 필수 입력값입니다.";

        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), message);

        return new ErrorResponse(message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException e, HttpServletRequest request) {
        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
