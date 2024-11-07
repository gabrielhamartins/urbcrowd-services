package com.unicamp.urbcrowd.exceptions.dto;

import org.springframework.http.HttpStatus;

public record ErrorDTO(
        HttpStatus error,
        String message
) {
}
