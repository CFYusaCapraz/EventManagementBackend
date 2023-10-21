package com.yusacapraz.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
