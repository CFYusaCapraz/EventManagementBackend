package com.yusacapraz.event.mapper;

import com.yusacapraz.event.model.DTOs.DistrictViewDTO;
import com.yusacapraz.event.model.District;

public class DistrictMapper {
    public static DistrictViewDTO viewMapper(District district) {
        return DistrictViewDTO.builder()
                .districtId(district.getDistrictId())
                .districtName(district.getDistrictName())
                .build();
    }
}
