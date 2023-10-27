package com.yusacapraz.event.service;

import com.yusacapraz.event.mapper.CityMapper;
import com.yusacapraz.event.response.APIResponse;
import com.yusacapraz.event.model.City;
import com.yusacapraz.event.model.DTOs.CityCreateDTO;
import com.yusacapraz.event.model.DTOs.CityUpdateDTO;
import com.yusacapraz.event.model.DTOs.CityViewDTO;
import com.yusacapraz.event.model.exception.CityNotFoundException;
import com.yusacapraz.event.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public ResponseEntity<APIResponse<?>> getAllCities() {
        try {
            List<City> cityList = cityRepository.findAll();
            List<CityViewDTO> viewDTOS = new ArrayList<>();
            if (cityList.isEmpty()) {
                APIResponse<Object> response = APIResponse.error("No city information is found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            for (City city : cityList) {
                viewDTOS.add(CityMapper.viewMapper(city));
            }
            APIResponse<List<CityViewDTO>> response = APIResponse.successWithData(viewDTOS, "Here are all cities.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> getCityById(Integer cityId) {
        try {
            if (cityId < 1) {
                APIResponse<Object> response = APIResponse.error("Please provide a city name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new CityNotFoundException("City of the given id not found!"));
            CityViewDTO viewDTO = CityMapper.viewMapper(city);
            APIResponse<CityViewDTO> response = APIResponse.successWithData(viewDTO, "City of the given id found.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CityNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> createCity(CityCreateDTO cityCreateDTO) {
        try {
            if (cityCreateDTO.getCityName().isEmpty()) {
                APIResponse<Object> response = APIResponse.error("Please provide a city name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            City city = City.builder().cityName(cityCreateDTO.getCityName()).build();
            cityRepository.saveAndFlush(city);
            CityViewDTO viewDTO = CityMapper.viewMapper(city);
            APIResponse<CityViewDTO> response = APIResponse.successWithData(viewDTO, "City of the given name is created successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataIntegrityViolationException e) {
            APIResponse<Object> response = APIResponse.error("City of the given name `%s` is already exists!".formatted(cityCreateDTO.getCityName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> updateCity(Integer cityId, CityUpdateDTO cityUpdateDTO) {
        try {
            if (cityId < 1) {
                APIResponse<Object> response = APIResponse.error("Please provide a valid city id!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new CityNotFoundException("City of the given id not found!"));
            if (cityUpdateDTO.getNewCityName().equals(city.getCityName())) {
                APIResponse<Object> response = APIResponse.error("Old name and the new name of the city cannot be the same!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            city.setCityName(cityUpdateDTO.getNewCityName());
            cityRepository.saveAndFlush(city);
            CityViewDTO viewDTO = CityMapper.viewMapper(city);
            APIResponse<CityViewDTO> response = APIResponse.successWithData(viewDTO, "City of the given id is updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CityNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DataIntegrityViolationException e) {
            APIResponse<Object> response = APIResponse.error("City of the given name `%s` is already exists!".formatted(cityUpdateDTO.getNewCityName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> deleteCity(Integer cityId) {
        try {
            if (cityId < 1) {
                APIResponse<Object> response = APIResponse.error("Please provide a valid city id!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new CityNotFoundException("City of the given id not found!"));
            cityRepository.delete(city);
            APIResponse<Object> response = APIResponse.success("City of the given id is deleted successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CityNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
