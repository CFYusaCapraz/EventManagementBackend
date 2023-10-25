package com.yusacapraz.event.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressCreateDTO {
    private String countryName;
    private String cityName;
    private String districtName;
    private String streetName;
    private Short doorNumber;
}
