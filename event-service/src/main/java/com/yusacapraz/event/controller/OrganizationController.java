package com.yusacapraz.event.controller;

import com.yusacapraz.event.model.DTOs.OrganizationCreateDTO;
import com.yusacapraz.event.model.DTOs.OrganizationUpdateDTO;
import com.yusacapraz.event.response.APIResponse;
import com.yusacapraz.event.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event/organization")
public class OrganizationController {
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<APIResponse<?>> getOrganizationByOrganizationId(@PathVariable("organizationId") String organizationId) {
        return organizationService.getOrganizationById(organizationId);
    }

    @PostMapping
    public ResponseEntity<APIResponse<?>> createOrganization(@RequestBody OrganizationCreateDTO organizationCreateDTO) {
        return organizationService.createOrganization(organizationCreateDTO);
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<APIResponse<?>> updateOrganization(@PathVariable("organizationId") String organizationId,
                                                             @RequestBody OrganizationUpdateDTO organizationUpdateDTO) {
        return organizationService.updateOrganization(organizationId, organizationUpdateDTO);
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<APIResponse<?>> deleteOrganization(@PathVariable("organizationId") String organizationId) {
        return organizationService.deleteOrganization(organizationId);
    }

}
