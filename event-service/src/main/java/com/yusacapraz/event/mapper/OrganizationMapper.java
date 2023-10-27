package com.yusacapraz.event.mapper;

import com.yusacapraz.event.model.DTOs.OrganizationViewDTO;
import com.yusacapraz.event.model.Organization;

public class OrganizationMapper {
    public static OrganizationViewDTO viewMapper(Organization organization) {
        return OrganizationViewDTO.builder()
                .organizationId(organization.getOrgId())
                .organizationName(organization.getOrgName())
                .organizationDetails(organization.getOrgDetails())
                .build();
    }
}
