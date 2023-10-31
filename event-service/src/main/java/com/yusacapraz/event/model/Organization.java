package com.yusacapraz.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "organizations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "org_id")
    private UUID orgId;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "org_details")
    private String orgDetails;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
