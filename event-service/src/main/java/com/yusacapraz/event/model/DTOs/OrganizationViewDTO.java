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
public class OrganizationViewDTO {
    private UUID organizationId;
    private String organizationName;
    private String organizationDetails;
}
