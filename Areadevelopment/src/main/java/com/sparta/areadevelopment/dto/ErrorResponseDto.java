package com.sparta.areadevelopment.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDto {
    private List<String> errors;
    private String message;

    public ErrorResponseDto(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }
}