package com.yusacapraz.event.service;

import com.yusacapraz.event.mapper.OrganizationMapper;
import com.yusacapraz.event.model.DTOs.OrganizationCreateDTO;
import com.yusacapraz.event.model.DTOs.OrganizationUpdateDTO;
import com.yusacapraz.event.model.DTOs.OrganizationViewDTO;
import com.yusacapraz.event.model.Organization;
import com.yusacapraz.event.model.exception.OrganizationNotFoundException;
import com.yusacapraz.event.repository.OrganizationRepository;
import com.yusacapraz.event.response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public ResponseEntity<APIResponse<?>> getAllOrganizations() {
        try {
            List<Organization> organizationList = organizationRepository.findAll();
            List<OrganizationViewDTO> viewDTOS = new ArrayList<>();
            if (organizationList.isEmpty()) {
                APIResponse<Object> response = APIResponse.error("No organization information is found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            for (Organization organization : organizationList) {
                viewDTOS.add(OrganizationMapper.viewMapper(organization));
            }
            APIResponse<List<OrganizationViewDTO>> response = APIResponse.successWithData(viewDTOS, "Here are all organizations.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> getOrganizationById(String organizationId) {
        try {
            UUID uuid = UUID.fromString(organizationId);
            Organization organization = organizationRepository.findById(uuid)
                    .orElseThrow(() -> new OrganizationNotFoundException("Organization of the given id not found!"));
            if (organization.getIsDeleted()) {
                APIResponse<Object> response = APIResponse.error("Organization of the id `%s` is deleted!".formatted(organizationId));
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            OrganizationViewDTO viewDTO = OrganizationMapper.viewMapper(organization);
            APIResponse<OrganizationViewDTO> response = APIResponse.successWithData(viewDTO, "Organization of given id is found.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (OrganizationNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            APIResponse<Object> response = APIResponse.error("Please enter a valid organization id!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> createOrganization(OrganizationCreateDTO organizationCreateDTO) {
        try {
            if (organizationCreateDTO.getOrganizationName().isEmpty()) {
                APIResponse<Object> response = APIResponse.error("Please enter an organization name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else if (organizationCreateDTO.getOrganizationDetails().isEmpty()) {
                APIResponse<Object> response = APIResponse.error("Please enter the organization details!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Organization organization = Organization.builder()
                    .orgName(organizationCreateDTO.getOrganizationName())
                    .orgDetails(organizationCreateDTO.getOrganizationDetails())
                    .build();
            organizationRepository.saveAndFlush(organization);
            OrganizationViewDTO viewDTO = OrganizationMapper.viewMapper(organization);
            APIResponse<OrganizationViewDTO> response = APIResponse.successWithData(viewDTO, "Organization of given id is found.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataIntegrityViolationException e) {
            APIResponse<Object> response = APIResponse.error("Organization of the given name `%s` is already exists!".formatted(organizationCreateDTO.getOrganizationName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> updateOrganization(String organizationId, OrganizationUpdateDTO organizationUpdateDTO) {
        try {
            UUID uuid = UUID.fromString(organizationId);
            Organization organization = organizationRepository.findById(uuid)
                    .orElseThrow(() -> new OrganizationNotFoundException("Organization of the given id not found!"));
            if (organizationUpdateDTO.isEmpty()) {
                APIResponse<Object> response = APIResponse.error("Please provide the necessary fields to update organization information!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            boolean anyUpdateApplied = false;
            if (!organizationUpdateDTO.getOrganizationNewName().isEmpty()
                    && !Objects.equals(organization.getOrgName(), organizationUpdateDTO.getOrganizationNewName())) {
                organization.setOrgName(organizationUpdateDTO.getOrganizationNewName());
                anyUpdateApplied = true;
            }
            if (!organizationUpdateDTO.getOrganizationNewDetails().isEmpty()
                    && !Objects.equals(organization.getOrgDetails(), organizationUpdateDTO.getOrganizationNewDetails())) {
                organization.setOrgDetails(organizationUpdateDTO.getOrganizationNewDetails());
                anyUpdateApplied = true;
            }
            if (anyUpdateApplied) {
                organizationRepository.saveAndFlush(organization);
                OrganizationViewDTO viewDTO = OrganizationMapper.viewMapper(organization);
                APIResponse<OrganizationViewDTO> response = APIResponse.successWithData(viewDTO, "Organization of the id `%s` is updated successfully.".formatted(organizationId));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            APIResponse<OrganizationViewDTO> response = APIResponse.error("No changes has been made!");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (IllegalArgumentException e) {
            APIResponse<Object> response = APIResponse.error("Please enter a valid organization id!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (OrganizationNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DataIntegrityViolationException e) {
            APIResponse<Object> response = APIResponse.error("Organization of the given name `%s` is already exists!".formatted(organizationUpdateDTO.getOrganizationNewName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> deleteOrganization(String organizationId) {
        try {
            UUID uuid = UUID.fromString(organizationId);
            Organization organization = organizationRepository.findById(uuid)
                    .orElseThrow(() -> new OrganizationNotFoundException("Organization of the given id not found!"));
            if (organization.getIsDeleted()) {
                APIResponse<Object> response = APIResponse.error("Organization of the id `%s` is already deleted!".formatted(organizationId));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            organization.setIsDeleted(true);
            organizationRepository.saveAndFlush(organization);
            APIResponse<OrganizationViewDTO> response = APIResponse.success("Organization of the id `%s` is deleted successfully.".formatted(organizationId));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (OrganizationNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            APIResponse<Object> response = APIResponse.error("Please enter a valid organization id!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
