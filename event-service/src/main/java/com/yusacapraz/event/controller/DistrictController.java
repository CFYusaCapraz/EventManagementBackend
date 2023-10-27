package com.yusacapraz.event.controller;

import com.yusacapraz.event.response.APIResponse;
import com.yusacapraz.event.model.DTOs.DistrictCreateDTO;
import com.yusacapraz.event.model.DTOs.DistrictUpdateDTO;
import com.yusacapraz.event.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event/district")
public class DistrictController {
    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllDistricts() {
        return districtService.getAllDistricts();
    }

    @GetMapping("/{districtId}")
    public ResponseEntity<APIResponse<?>> getDistrictByDistrictId(@PathVariable("districtId") Integer districtId) {
        return districtService.getDistrictById(districtId);
    }

    @PostMapping
    public ResponseEntity<APIResponse<?>> createDistrict(@RequestBody DistrictCreateDTO districtCreateDTO) {
        return districtService.createDistrict(districtCreateDTO);
    }

    @PutMapping("/{districtId}")
    public ResponseEntity<APIResponse<?>> updateDistrict(@PathVariable("districtId") Integer districtId,
                                                         @RequestBody DistrictUpdateDTO districtUpdateDTO) {
        return districtService.updateDistrict(districtId, districtUpdateDTO);
    }

    @DeleteMapping("/{districtId}")
    public ResponseEntity<APIResponse<?>> deleteDistrict(@PathVariable("districtId") Integer districtId) {
        return districtService.deleteDistrict(districtId);
    }

}
