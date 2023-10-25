package com.yusacapraz.event.controller;

import com.yusacapraz.event.model.APIResponse;
import com.yusacapraz.event.model.DTOs.AddressCreateDTO;
import com.yusacapraz.event.model.DTOs.AddressUpdateDTO;
import com.yusacapraz.event.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event/address")
public class AddressController {
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllAddress() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<APIResponse<?>> getAddressById(@PathVariable("addressId") String addressId) {
        return addressService.getAddressById(addressId);
    }

    @PostMapping
    public ResponseEntity<APIResponse<?>> createAddress(@RequestBody AddressCreateDTO addressCreateDTO) {
        return addressService.createAddress(addressCreateDTO);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<APIResponse<?>> updateAddress(@PathVariable("addressId") String addressId,
                                                        @RequestBody AddressUpdateDTO addressUpdateDTO) {
        return addressService.updateAddress(addressId, addressUpdateDTO);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<APIResponse<?>> deleteAddress(@PathVariable("addressId") String addressId) {
        return addressService.deleteAddress(addressId);
    }
}
