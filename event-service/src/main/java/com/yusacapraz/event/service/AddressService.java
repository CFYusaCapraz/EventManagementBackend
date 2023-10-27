package com.yusacapraz.event.service;

import com.yusacapraz.event.mapper.AddressMapper;
import com.yusacapraz.event.response.APIResponse;
import com.yusacapraz.event.model.Address;
import com.yusacapraz.event.model.DTOs.AddressCreateDTO;
import com.yusacapraz.event.model.DTOs.AddressUpdateDTO;
import com.yusacapraz.event.model.DTOs.AddressViewDTO;
import com.yusacapraz.event.model.exception.AddressNotFoundException;
import com.yusacapraz.event.model.exception.CityNotFoundException;
import com.yusacapraz.event.model.exception.CountryNotFoundException;
import com.yusacapraz.event.model.exception.DistrictNotFoundException;
import com.yusacapraz.event.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public ResponseEntity<APIResponse<?>> getAllAddresses() {
        try {
            List<Address> addressList = addressRepository.findAll();
            List<AddressViewDTO> viewDTOS = new ArrayList<>();
            if (addressList.isEmpty()) {
                APIResponse<Object> response = APIResponse.error("No address information is found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            for (Address address : addressList) {
                viewDTOS.add(addressMapper.viewMapper(address));
            }
            APIResponse<List<AddressViewDTO>> response = APIResponse.successWithData(viewDTOS, "Here are all address.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> getAddressById(String addressId) {
        try {
            UUID uuid = UUID.fromString(addressId);
            Address address = addressRepository.findById(uuid)
                    .orElseThrow(() -> new AddressNotFoundException("Address of the given id not found!"));
            AddressViewDTO viewDTO = addressMapper.viewMapper(address);
            APIResponse<AddressViewDTO> response = APIResponse.successWithData(viewDTO, "Address of given id is found.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (AddressNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            APIResponse<Object> response = APIResponse.error("Please enter a valid address id!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> createAddress(AddressCreateDTO addressCreateDTO) {
        try {
            Address address = addressMapper.createMapper(addressCreateDTO);
            addressRepository.saveAndFlush(address);
            AddressViewDTO viewDTO = addressMapper.viewMapper(address);
            APIResponse<AddressViewDTO> response = APIResponse.successWithData(viewDTO, "New address successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CountryNotFoundException | DistrictNotFoundException | CityNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> updateAddress(String addressId, AddressUpdateDTO addressUpdateDTO) {
        try {
            UUID uuid = UUID.fromString(addressId);
            Address address = addressRepository.findById(uuid)
                    .orElseThrow(() -> new AddressNotFoundException("Address of the given id not found!"));
            Address updatedAddress = addressMapper.updateMapper(address, addressUpdateDTO);
            AddressViewDTO viewDTO = addressMapper.viewMapper(updatedAddress);
            APIResponse<AddressViewDTO> response = APIResponse.successWithData(viewDTO, "Address information successfully updated.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (AddressNotFoundException | CountryNotFoundException | DistrictNotFoundException |
                 CityNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            APIResponse<Object> response = APIResponse.error("Please enter a valid address id!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> deleteAddress(String addressId) {
        try {
            UUID uuid = UUID.fromString(addressId);
            Address address = addressRepository.findById(uuid)
                    .orElseThrow(() -> new AddressNotFoundException("Address of the given id not found!"));
            addressRepository.delete(address);
            APIResponse<Object> response = APIResponse.success("Address of given id is deleted successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (AddressNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            APIResponse<Object> response = APIResponse.error("Please enter a valid address id!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
