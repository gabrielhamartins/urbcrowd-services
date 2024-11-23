package com.unicamp.urbcrowd.controllers.dto;

public record UserInfoResponseDTO(
        String id,
        String name,
        String username,
        String email,
        String pictureHref
) {
}
