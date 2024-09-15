package com.unicamp.urbcrowd.controllers.dto;

import com.unicamp.urbcrowd.models.Address;
import com.unicamp.urbcrowd.models.Geolocation;

public record ComplaintDTO (
        Address address,
        Geolocation geolocation,
        String description
){
}
