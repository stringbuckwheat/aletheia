package com.gold.auth.common.error;

import com.gold.auth.common.error.exception.HasSameUsernameException;
import com.gold.auth.common.error.exception.RefreshTokenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : ErrorMessage.REQUEST_VALIDATION_FAILED.getMessage();

        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), errorMessage);

        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler(HasSameUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleHasSameUsernameException(HasSameUsernameException e, HttpServletRequest request) {
        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request) {
        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleRefreshTokenException(RefreshTokenException e, HttpServletRequest request) {
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
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotAllowed(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("threw {} in endpoint: {} with message: {}",
                e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());

        return new ErrorResponse(e.getMessage());
    }
}
