package com.yusacapraz.event.service;

import com.yusacapraz.event.mapper.DistrictMapper;
import com.yusacapraz.event.model.DTOs.DistrictCreateDTO;
import com.yusacapraz.event.model.DTOs.DistrictUpdateDTO;
import com.yusacapraz.event.model.DTOs.DistrictViewDTO;
import com.yusacapraz.event.model.District;
import com.yusacapraz.event.model.exception.DistrictNotFoundException;
import com.yusacapraz.event.repository.DistrictRepository;
import com.yusacapraz.event.response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public ResponseEntity<APIResponse<?>> getAllDistricts() {
        try {
            List<District> districtList = districtRepository.findAll();
            List<DistrictViewDTO> viewDTOS = new ArrayList<>();
            if (districtList.isEmpty()) {
                APIResponse<Object> response = APIResponse.error("No district information is found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            for (District district : districtList) {
                viewDTOS.add(DistrictMapper.viewMapper(district));
            }
            APIResponse<List<DistrictViewDTO>> response = APIResponse.successWithData(viewDTOS, "Here are all cities.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> getDistrictById(Integer districtId) {
        try {
            if (districtId < 1) {
                APIResponse<Object> response = APIResponse.error("Please provide a district name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            District district = districtRepository.findById(districtId)
                    .orElseThrow(() -> new DistrictNotFoundException("District of the given id not found!"));
            DistrictViewDTO viewDTO = DistrictMapper.viewMapper(district);
            APIResponse<DistrictViewDTO> response = APIResponse.successWithData(viewDTO, "District of the given id found.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DistrictNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> createDistrict(DistrictCreateDTO districtCreateDTO) {
        try {
            if (districtCreateDTO.getDistrictName().isEmpty()) {
                APIResponse<Object> response = APIResponse.error("Please provide a district name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            District district = District.builder().districtName(districtCreateDTO.getDistrictName()).build();
            districtRepository.saveAndFlush(district);
            DistrictViewDTO viewDTO = DistrictMapper.viewMapper(district);
            APIResponse<DistrictViewDTO> response = APIResponse.successWithData(viewDTO, "District of the given name is created successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataIntegrityViolationException e) {
            APIResponse<Object> response = APIResponse.error("District of the given name `%s` is already exists!".formatted(districtCreateDTO.getDistrictName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> updateDistrict(Integer districtId, DistrictUpdateDTO districtUpdateDTO) {
        try {
            if (districtId < 1) {
                APIResponse<Object> response = APIResponse.error("Please provide a valid district id!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            District district = districtRepository.findById(districtId)
                    .orElseThrow(() -> new DistrictNotFoundException("District of the given id not found!"));
            if (districtUpdateDTO.getNewDistrictName().isEmpty()) {
                APIResponse<Object> response = APIResponse.error("Please provide a valid district name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (districtUpdateDTO.getNewDistrictName().equals(district.getDistrictName())) {
                APIResponse<Object> response = APIResponse.error("Old name and the new name of the district cannot be the same!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            district.setDistrictName(districtUpdateDTO.getNewDistrictName());
            districtRepository.saveAndFlush(district);
            DistrictViewDTO viewDTO = DistrictMapper.viewMapper(district);
            APIResponse<DistrictViewDTO> response = APIResponse.successWithData(viewDTO, "District of the given id is updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DistrictNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DataIntegrityViolationException e) {
            APIResponse<Object> response = APIResponse.error("District of the given name `%s` is already exists!".formatted(districtUpdateDTO.getNewDistrictName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> deleteDistrict(Integer districtId) {
        try {
            if (districtId < 1) {
                APIResponse<Object> response = APIResponse.error("Please provide a valid district id!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            District district = districtRepository.findById(districtId)
                    .orElseThrow(() -> new DistrictNotFoundException("District of the given id not found!"));
            districtRepository.delete(district);
            APIResponse<Object> response = APIResponse.success("District of the given id is deleted successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DistrictNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
