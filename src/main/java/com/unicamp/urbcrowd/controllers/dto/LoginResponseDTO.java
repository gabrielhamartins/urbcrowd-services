package com.unicamp.urbcrowd.controllers.dto;

import java.util.List;

public record LoginResponseDTO(
        String accessToken,
        Long expiresIn,
        List<String> roles
) {
}
