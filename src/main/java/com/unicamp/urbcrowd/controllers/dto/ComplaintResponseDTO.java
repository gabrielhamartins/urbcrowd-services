package com.unicamp.urbcrowd.controllers.dto;

import com.unicamp.urbcrowd.models.Address;
import com.unicamp.urbcrowd.models.ComplaintStatus;
import com.unicamp.urbcrowd.models.ComplaintType;
import com.unicamp.urbcrowd.models.Geolocation;

import java.time.LocalDateTime;
import java.util.Set;

public record ComplaintResponseDTO(
        String id,
        String title,
        String userEmail,
        Address address,
        Geolocation geolocation,
        String imageHref,
        LocalDateTime createdDate,
        String description,
        ComplaintStatus status,
        ComplaintType type,
        Set<CommentDTO> comments,
        int thumbsUpCount,
        boolean userHasThumbsUp
) {
}
