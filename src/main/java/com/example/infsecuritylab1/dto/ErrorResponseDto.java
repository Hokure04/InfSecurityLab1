package com.example.infsecuritylab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
    private String path;
    private int status;
    private LocalDateTime timestamp;
}
