package com.yusacapraz.event.mapper;

import com.yusacapraz.event.model.Country;
import com.yusacapraz.event.model.DTOs.CountryViewDTO;

public class CountryMapper {
    public static CountryViewDTO viewMapper(Country country) {
        return CountryViewDTO.builder()
                .countryId(country.getCountryId())
                .countryName(country.getCountryName())
                .build();
    }
}
