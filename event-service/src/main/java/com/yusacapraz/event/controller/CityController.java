package com.yusacapraz.event.controller;

import com.yusacapraz.event.model.APIResponse;
import com.yusacapraz.event.model.DTOs.CityCreateDTO;
import com.yusacapraz.event.model.DTOs.CityUpdateDTO;
import com.yusacapraz.event.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event/city")
public class CityController {
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<APIResponse<?>> getCityByCityId(@PathVariable("cityId") Integer cityId) {
        return cityService.getCityById(cityId);
    }

    @PostMapping
    public ResponseEntity<APIResponse<?>> createCity(@RequestBody CityCreateDTO cityCreateDTO){
        return cityService.createCity(cityCreateDTO);
    }

    @PutMapping("/{cityId}")
    public ResponseEntity<APIResponse<?>> updateCity(@PathVariable("cityId") Integer cityId,
                                                     @RequestBody CityUpdateDTO cityUpdateDTO){
        return cityService.updateCity(cityId, cityUpdateDTO);
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<APIResponse<?>> deleteCity(@PathVariable("cityId") Integer cityId){
        return cityService.deleteCity(cityId);
    }

}
