package com.example.infsecuritylab1.exception;

import com.example.infsecuritylab1.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtExpired(JwtTokenExpiredException ex, HttpServletRequest request){
        return createErrorResponse(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizeException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorize(AuthorizeException ex, HttpServletRequest request){
        return createErrorResponse(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FieldNotSpecifiedException.class)
    public ResponseEntity<ErrorResponseDto> handleFieldNotSpecified(FieldNotSpecifiedException ex, HttpServletRequest request){
        return createErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleStandardExceptions(Exception ex, HttpServletRequest request){
        log.error("Unexpected error occurred: ", ex);
        return createErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BlockedUserException.class)
    public ResponseEntity<ErrorResponseDto> handleBlockedUser(BlockedUserException ex, HttpServletRequest request){
        return createErrorResponse(ex, request, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ErrorResponseDto> createErrorResponse(Exception ex, HttpServletRequest request, HttpStatus status){
        log.error("Error occurred: {}", ex.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(
                ex.getMessage(),
                request.getRequestURI(),
                status.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(response);
    }
}