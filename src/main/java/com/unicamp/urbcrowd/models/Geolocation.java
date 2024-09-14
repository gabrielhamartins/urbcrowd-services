package com.unicamp.urbcrowd.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Geolocation {

    private Double latitude;
    private Double longitude;
}
