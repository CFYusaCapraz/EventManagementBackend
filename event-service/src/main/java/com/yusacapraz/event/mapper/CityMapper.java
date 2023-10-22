package com.yusacapraz.event.mapper;

import com.yusacapraz.event.model.City;
import com.yusacapraz.event.model.DTOs.CityViewDTO;

public class CityMapper {
    public static CityViewDTO viewMapper(City city) {
        return CityViewDTO.builder()
                .cityId(city.getCityId())
                .cityName(city.getCityName())
                .build();
    }
}
