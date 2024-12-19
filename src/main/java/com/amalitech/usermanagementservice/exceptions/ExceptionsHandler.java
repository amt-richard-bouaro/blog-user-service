package com.amalitech.usermanagementservice.exceptions;

import com.amalitech.usermanagementservice.dto.global.ErrorResponse;
import com.amalitech.usermanagementservice.enums.ErrorCode;
import jakarta.servlet.UnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.lang.model.type.NullType;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse<NullType>> handlerConflictException(
            ConflictException exception,
            WebRequest request
    ) {
        ErrorResponse<NullType> responseDTO = new ErrorResponse<>(ErrorCode.ERR_UNKNOWN, exception.getMessage(),
                Instant.now()
                        .toString(), request.getDescription(false).substring(4),null);
        return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse<NullType>> handlerNotFoundException(
            NotFoundException exception,
            WebRequest request
    ) {
        ErrorResponse<NullType> responseDTO = new ErrorResponse<>(ErrorCode.ERR_UNKNOWN, exception.getMessage(),
                Instant.now()
                        .toString(),request.getDescription(false).substring(4), null);
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse<NullType>> handlerForbiddenException(
            ForbiddenException exception,
            WebRequest request
    ) {
        ErrorResponse<NullType> responseDTO = new ErrorResponse<>(ErrorCode.ERR_UNKNOWN, exception.getMessage(),
                Instant.now()
                        .toString(), request.getDescription(false).substring(4),null);
        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse<NullType>> handlerInternalServerErrorException(
            InternalServerErrorException exception,
            WebRequest request
    ) {

        ErrorResponse<NullType> responseDTO = new ErrorResponse<>(ErrorCode.ERR_UNKNOWN, exception.getMessage(),
                Instant.now()
                        .toString(), request.getDescription(false).substring(4),null);
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse<NullType>> handlerUnauthorizedException(
            UnauthorizedException exception,
            WebRequest request
    ) {

        ErrorResponse<NullType> responseDTO = new ErrorResponse<>(ErrorCode.ERR_UNKNOWN, exception.getMessage(),
                Instant.now()
                        .toString(), request.getDescription(false).substring(4),null);
        return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(UnavailableException.class)
    public ResponseEntity<ErrorResponse<NullType>> handlerUnavailableException(
            UnavailableException exception,
            WebRequest request
    ) {

        ErrorResponse<NullType> responseDTO = new ErrorResponse<>(ErrorCode.ERR_UNKNOWN, exception.getMessage(),
                Instant.now()
                        .toString(), request.getDescription(false).substring(4),null);
        return new ResponseEntity<>(responseDTO, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse<NullType>> handlerUsernameNotFoundException(
            UsernameNotFoundException exception,
            WebRequest request
    ) {

        ErrorResponse<NullType> responseDTO = new ErrorResponse<>(ErrorCode.ERR_UNKNOWN, exception.getMessage(),
                Instant.now()
                        .toString(), request.getDescription(false).substring(4),null);
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Map<String, String>>> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    if (error instanceof FieldError fieldError) {
                        errors.put("field", fieldError.getField());
                        errors.put("cause", fieldError.getDefaultMessage());
                    } else {
                        errors.put(error.getObjectName(), error.getDefaultMessage());
                    }
                });

        ErrorResponse<Map<String, String>> responseDTO = new ErrorResponse<>(ErrorCode.ERR_UNKNOWN,
                "Validation Error: Payload not well formed", Instant.now()
                .toString(),request.getDescription(false).substring(4), errors);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

}
