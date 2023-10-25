package com.yusacapraz.event.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressViewDTO {
    private UUID addressId;
    private String countryName;
    private String cityName;
    private String districtName;
    private String streetName;
    private Short doorNumber;
}
