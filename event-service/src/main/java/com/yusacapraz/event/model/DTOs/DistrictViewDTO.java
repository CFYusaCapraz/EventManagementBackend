package com.yusacapraz.event.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DistrictViewDTO {
    private Integer districtId;
    private String districtName;
}
