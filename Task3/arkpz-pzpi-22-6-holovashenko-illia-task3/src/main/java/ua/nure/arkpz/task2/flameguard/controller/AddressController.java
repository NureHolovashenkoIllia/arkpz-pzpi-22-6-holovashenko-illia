package ua.nure.arkpz.task2.flameguard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.AddressDto;
import ua.nure.arkpz.task2.flameguard.service.AddressService;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing addresses.
 * Provides endpoints for CRUD operations on addresses.
 */
@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Addresses", description = "Endpoints for managing addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * Retrieves all addresses.
     *
     * @return a list of all addresses.
     */
    @Operation(summary = "Retrieve all addresses", description = "Returns a list of all addresses in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of addresses",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        List<AddressDto> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    /**
     * Retrieves the address for a specific building by building ID.
     *
     * @param buildingId the ID of the building.
     * @return the address associated with the specified building, or a 404 error if not found.
     */
    @Operation(summary = "Retrieve address by building ID", description = "Returns the address associated with the specified building ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the address",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDto.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "application/json"))
    })
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

    /**
     * Creates a new address.
     *
     * @param addressDto the address data to create.
     * @return the created address.
     */
    @Operation(summary = "Create a new address", description = "Creates a new address in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json"))
    })
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

    /**
     * Updates an existing address by ID.
     *
     * @param id                the ID of the address to update.
     * @param updatedAddressDto the updated address data.
     * @return the updated address.
     */
    @Operation(summary = "Update an address", description = "Updates the details of an existing address by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDto.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "application/json"))
    })
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

    /**
     * Updates the building ID associated with an address.
     *
     * @param id         the ID of the address to update.
     * @param buildingId the new building ID.
     * @return the updated address.
     */
    @Operation(summary = "Update building ID for an address", description = "Updates the building ID field for a specific address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Building ID successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AddressDto.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "application/json"))
    })
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

    /**
     * Deletes an address by ID.
     *
     * @param id the ID of the address to delete.
     * @return a no-content response if the deletion is successful.
     */
    @Operation(summary = "Delete an address", description = "Deletes an existing address by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(mediaType = "application/json"))
    })
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