package com.unicamp.urbcrowd.controllers.dto;

import com.unicamp.urbcrowd.models.ComplaintStatus;

public record ComplaitStatusUpdateRequestDTO(
        ComplaintStatus status
) {
}
