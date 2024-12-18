package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.AddressDto;
import ua.nure.arkpz.task2.flameguard.service.AddressService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Retrieve all addresses
    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        List<AddressDto> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    // Retrieve address for a specific building
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<?> getAddressByBuildingId(@PathVariable int buildingId) {
        Optional<AddressDto> address = addressService.getAddressByBuildingId(buildingId);

        if (address.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(address);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\":\"No address found for building with id - " + buildingId + "\"}");
    }

    // Create a new address
    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody AddressDto addressDto) {
        try {
            Optional<AddressDto> createdAddress = addressService.createAddress(addressDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdAddress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // Update an existing address
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable int id,
                                           @RequestBody AddressDto updatedAddressDto) {
        try {
            Optional<AddressDto> updatedAddress =
                    addressService.updateAddress(id, updatedAddressDto);
            return ResponseEntity.ok(updatedAddress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // Update building_id field for a specific address
    @PatchMapping("/{id}/building")
    public ResponseEntity<?> updateAddressBuilding(@PathVariable int id,
                                                      @RequestParam int buildingId) {
        try {
            Optional<AddressDto> updatedAddress =
                    addressService.updateAddressBuilding(id, buildingId);
            return ResponseEntity.ok(updatedAddress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // Delete an address
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable int id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}

