package com.yusacapraz.event.service;

import com.yusacapraz.event.mapper.CountryMapper;
import com.yusacapraz.event.model.Country;
import com.yusacapraz.event.model.DTOs.CountryCreateDTO;
import com.yusacapraz.event.model.DTOs.CountryUpdateDTO;
import com.yusacapraz.event.model.DTOs.CountryViewDTO;
import com.yusacapraz.event.model.exception.CountryNotFoundException;
import com.yusacapraz.event.repository.CountryRepository;
import com.yusacapraz.event.response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public ResponseEntity<APIResponse<?>> getAllCountries() {
        try {
            List<Country> countryList = countryRepository.findAll();
            List<CountryViewDTO> viewDTOS = new ArrayList<>();
            if (countryList.isEmpty()) {
                APIResponse<Object> response = APIResponse.error("No country information is found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            for (Country country : countryList) {
                viewDTOS.add(CountryMapper.viewMapper(country));
            }
            APIResponse<List<CountryViewDTO>> response = APIResponse.successWithData(viewDTOS, "Here are all countries.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> getCountryById(Integer countryId) {
        try {
            if (countryId < 1) {
                APIResponse<Object> response = APIResponse.error("Please provide a country name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new CountryNotFoundException("Country of the given id not found!"));
            CountryViewDTO viewDTO = CountryMapper.viewMapper(country);
            APIResponse<CountryViewDTO> response = APIResponse.successWithData(viewDTO, "Country of the given id found.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CountryNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> createCountry(CountryCreateDTO countryCreateDTO) {
        try {
            if (countryCreateDTO.getCountryName().isEmpty()) {
                APIResponse<Object> response = APIResponse.error("Please provide a country name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Country country = Country.builder().countryName(countryCreateDTO.getCountryName()).build();
            countryRepository.saveAndFlush(country);
            CountryViewDTO viewDTO = CountryMapper.viewMapper(country);
            APIResponse<CountryViewDTO> response = APIResponse.successWithData(viewDTO, "Country of the given name is created successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataIntegrityViolationException e) {
            APIResponse<Object> response = APIResponse.error("Country of the given name `%s` is already exists!".formatted(countryCreateDTO.getCountryName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> updateCountry(Integer countryId, CountryUpdateDTO countryUpdateDTO) {
        try {
            if (countryId < 1) {
                APIResponse<Object> response = APIResponse.error("Please provide a valid country id!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new CountryNotFoundException("Country of the given id not found!"));
            if (countryUpdateDTO.getNewCountryName().isEmpty()) {
                APIResponse<Object> response = APIResponse.error("Please provide a valid country name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (countryUpdateDTO.getNewCountryName().equals(country.getCountryName())) {
                APIResponse<Object> response = APIResponse.error("Old name and the new name of the country cannot be the same!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            country.setCountryName(countryUpdateDTO.getNewCountryName());
            countryRepository.saveAndFlush(country);
            CountryViewDTO viewDTO = CountryMapper.viewMapper(country);
            APIResponse<CountryViewDTO> response = APIResponse.successWithData(viewDTO, "Country of the given id is updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CountryNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DataIntegrityViolationException e) {
            APIResponse<Object> response = APIResponse.error("Country of the given name `%s` is already exists!".formatted(countryUpdateDTO.getNewCountryName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> deleteCountry(Integer countryId) {
        try {
            if (countryId < 1) {
                APIResponse<Object> response = APIResponse.error("Please provide a valid country id!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new CountryNotFoundException("Country of the given id not found!"));
            countryRepository.delete(country);
            APIResponse<Object> response = APIResponse.success("Country of the given id is deleted successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CountryNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
