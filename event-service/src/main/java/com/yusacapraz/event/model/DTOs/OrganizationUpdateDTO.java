package com.yusacapraz.event.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrganizationUpdateDTO {
    private String organizationNewName;
    private String organizationNewDetails;

    public Boolean isEmpty() {
        return organizationNewName.isEmpty() && organizationNewDetails.isEmpty();
    }
}
