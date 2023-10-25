package com.yusacapraz.event.mapper;

import com.yusacapraz.event.model.Address;
import com.yusacapraz.event.model.City;
import com.yusacapraz.event.model.Country;
import com.yusacapraz.event.model.DTOs.AddressCreateDTO;
import com.yusacapraz.event.model.DTOs.AddressUpdateDTO;
import com.yusacapraz.event.model.DTOs.AddressViewDTO;
import com.yusacapraz.event.model.District;
import com.yusacapraz.event.model.exception.CityNotFoundException;
import com.yusacapraz.event.model.exception.CountryNotFoundException;
import com.yusacapraz.event.model.exception.DistrictNotFoundException;
import com.yusacapraz.event.repository.CityRepository;
import com.yusacapraz.event.repository.CountryRepository;
import com.yusacapraz.event.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AddressMapper {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;

    @Autowired
    public AddressMapper(CountryRepository countryRepository, CityRepository cityRepository, DistrictRepository districtRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
    }

    public AddressViewDTO viewMapper(Address address) {
        return AddressViewDTO.builder()
                .addressId(address.getAddressId())
                .countryName(address.getCountry().getCountryName())
                .cityName(address.getCity().getCityName())
                .districtName(address.getDistrict().getDistrictName())
                .streetName(address.getStreetName())
                .doorNumber(address.getDoorNumber())
                .build();
    }

    public Address createMapper(AddressCreateDTO addressCreateDTO) throws
            CountryNotFoundException,
            CityNotFoundException,
            DistrictNotFoundException {
        return Address.builder()
                .country(getCountryIfExists(addressCreateDTO.getCountryName()))
                .city(getCityIfExists(addressCreateDTO.getCityName()))
                .district(getDistrictIfExists(addressCreateDTO.getDistrictName()))
                .streetName(addressCreateDTO.getStreetName())
                .doorNumber(addressCreateDTO.getDoorNumber())
                .build();
    }

    public Address updateMapper(Address address, AddressUpdateDTO addressUpdateDTO) throws
            CountryNotFoundException,
            CityNotFoundException,
            DistrictNotFoundException {

        if (addressUpdateDTO.getNewDoorNumber() != null
                && !Objects.equals(address.getDoorNumber(), addressUpdateDTO.getNewDoorNumber())) {
            address.setDoorNumber(addressUpdateDTO.getNewDoorNumber());
        }

        if (addressUpdateDTO.getNewStreetName() != null
                && !Objects.equals(address.getStreetName(), addressUpdateDTO.getNewStreetName())) {
            address.setStreetName(addressUpdateDTO.getNewStreetName());
        }

        if (addressUpdateDTO.getNewCountryName() != null
                && !Objects.equals(address.getCountry().getCountryName(), addressUpdateDTO.getNewCountryName())) {
            address.setCountry(getCountryIfExists(addressUpdateDTO.getNewCountryName()));
        }
        // complete the other fields
        return address;
    }

    private Country getCountryIfExists(String countryName) {
        return countryRepository.findCountryByCountryName(countryName)
                .orElseThrow(() -> new CountryNotFoundException("Country of the given name not found!"));
    }

    private City getCityIfExists(String cityName) {
        return cityRepository.findCityByCityName(cityName)
                .orElseThrow(() -> new CityNotFoundException("City of the given name not found!"));
    }

    private District getDistrictIfExists(String districtName) {
        return districtRepository.findDistrictByDistrictName(districtName)
                .orElseThrow(() -> new DistrictNotFoundException("District of the given name not found!"));
    }

}
