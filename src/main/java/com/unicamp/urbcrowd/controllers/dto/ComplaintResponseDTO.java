package com.unicamp.urbcrowd.controllers.dto;

import com.unicamp.urbcrowd.models.*;

import java.time.LocalDateTime;
import java.util.Set;

public record ComplaintResponseDTO(
        String id,
        String userEmail,
        Address address,
        Geolocation geolocation,
        String imageHref,
        LocalDateTime createdDate,
        String description,
        ComplaintStatus status,
        ComplaintType type,
        Set<Comment> comments,
        int thumbsUpCount
) {
}
