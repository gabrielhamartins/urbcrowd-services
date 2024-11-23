package com.unicamp.urbcrowd.controllers.dto;

import com.unicamp.urbcrowd.models.Address;
import com.unicamp.urbcrowd.models.ComplaintType;
import com.unicamp.urbcrowd.models.Geolocation;

public record ComplaintRequestDTO(
        Address address,
        Geolocation geolocation,
        ComplaintType type,
        String description
) {
}
