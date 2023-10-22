package com.yusacapraz.event.controller;

import com.yusacapraz.event.model.APIResponse;
import com.yusacapraz.event.model.DTOs.CountryCreateDTO;
import com.yusacapraz.event.model.DTOs.CountryUpdateDTO;
import com.yusacapraz.event.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event/country")
public class CountryController {
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<APIResponse<?>> getCountryByCountryId(@PathVariable("countryName") Integer countryId) {
        return countryService.getCountryById(countryId);
    }

    @PostMapping
    public ResponseEntity<APIResponse<?>> createCountry(@RequestBody CountryCreateDTO countryCreateDTO) {
        return countryService.createCountry(countryCreateDTO);
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<APIResponse<?>> updateCountry(@PathVariable("countryId") Integer countryId,
                                                        @RequestBody CountryUpdateDTO countryUpdateDTO) {
        return countryService.updateCountry(countryId, countryUpdateDTO);
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<APIResponse<?>> deleteCountry(@PathVariable("countryId") Integer countryId) {
        return countryService.deleteCountry(countryId);
    }

}
