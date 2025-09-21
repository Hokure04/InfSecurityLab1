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

    @ExceptionHandler({AuthorizeException.class, FieldNotSpecifiedException.class, JwtTokenExpiredException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptions(Exception ex, HttpServletRequest request){
        log.error("Error occurred: {}", ex.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleStandardExceptions(Exception ex, HttpServletRequest request){
        log.error("Unexpected error occurred: ", ex);

        ErrorResponseDto response = new ErrorResponseDto(
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}