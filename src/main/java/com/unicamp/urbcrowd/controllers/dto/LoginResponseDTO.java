package com.unicamp.urbcrowd.controllers.dto;

public record LoginResponseDTO(
        String accessToken,
        Long expiresIn
) {
}
