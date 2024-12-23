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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.BuildingDto;
import ua.nure.arkpz.task2.flameguard.dto.MaintenanceDto;
import ua.nure.arkpz.task2.flameguard.dto.PaymentDto;
import ua.nure.arkpz.task2.flameguard.service.MaintenanceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for handling maintenance-related API requests.
 */
@RestController
@RequestMapping("/api/maintenances")
@Tag(name = "Maintenances", description = "Endpoints for handling maintenance-related API requests")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    /**
     * Retrieve all maintenances.
     *
     * @return List of MaintenanceDto objects.
     */
    @Operation(summary = "Get all maintenances", description = "Retrieve all maintenance records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved maintenances",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MaintenanceDto.class))),
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @GetMapping
    public ResponseEntity<List<MaintenanceDto>> getAllMaintenances() {
        List<MaintenanceDto> maintenances = maintenanceService.getAllMaintenances();
        return ResponseEntity.ok(maintenances);
    }

    /**
     * Retrieve a specific maintenance by ID.
     *
     * @param id Maintenance ID.
     * @return MaintenanceDto object or an error message if not found.
     */
    @Operation(summary = "Get maintenance by ID", description = "Retrieve a specific maintenance by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MaintenanceDto.class))),
            @ApiResponse(responseCode = "404", description = "Maintenance not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No maintenance found with specified id\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getMaintenanceById(@PathVariable int id) {
        Optional<MaintenanceDto> maintenance = maintenanceService.getMaintenanceById(id);

        if (maintenance.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(maintenance);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\":\"No maintenance found with id - " + id + "\"}");
    }

    /**
     * Retrieve all maintenances for a specific building.
     *
     * @param buildingId Building ID.
     * @return List of MaintenanceDto objects or an error message if none found.
     */
    @Operation(summary = "Get maintenances by building ID", description = "Retrieve maintenances associated with a specific building.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MaintenanceDto.class))),
            @ApiResponse(responseCode = "404", description = "No maintenances found for the building",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No maintenances found for building with specified id\"}")))
    })
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<?> getMaintenancesByBuildingId(@PathVariable int buildingId) {
        List<MaintenanceDto> maintenances = maintenanceService
                .getMaintenancesByBuildingId(buildingId);

        if (maintenances.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No maintenances found for building with id - " + buildingId + "\"}");
        }
        return ResponseEntity.ok(maintenances);
    }

    /**
     * Create a new maintenance record.
     *
     * @param maintenanceDto Maintenance data.
     * @return The created MaintenanceDto object or an error message.
     */
    @Operation(summary = "Create maintenance", description = "Create a new maintenance record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Maintenance created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MaintenanceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or error during creation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"Invalid maintenance data\"}")))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @PostMapping
    public ResponseEntity<?> createMaintenance(@RequestBody MaintenanceDto maintenanceDto) {
        try {
            Optional<MaintenanceDto> createdMaintenance = maintenanceService.createMaintenance(maintenanceDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdMaintenance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Update an existing maintenance record.
     *
     * @param id Maintenance ID.
     * @param updatedMaintenanceDto Updated maintenance data.
     * @return The updated MaintenanceDto object or an error message.
     */
    @Operation(summary = "Update maintenance", description = "Update an existing maintenance record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MaintenanceDto.class))),
            @ApiResponse(responseCode = "404", description = "Maintenance not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No maintenance found with specified id\"}")))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaintenance(@PathVariable int id,
                                               @RequestBody MaintenanceDto updatedMaintenanceDto) {
        try {
            Optional<MaintenanceDto> updatedMaintenance = maintenanceService
                    .updateMaintenance(id, updatedMaintenanceDto);
            return ResponseEntity.ok(updatedMaintenance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Delete a maintenance record.
     *
     * @param id Maintenance ID.
     * @return No content response or an error message if not found.
     */
    @Operation(summary = "Delete maintenance", description = "Delete a maintenance record by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Maintenance deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Maintenance not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No maintenance found with specified id\"}")))
    })
    @PreAuthorize("hasAnyAuthority('Global_Administrator')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaintenance(@PathVariable int id) {
        try {
            maintenanceService.deleteMaintenance(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PreAuthorize("hasAnyAuthority('System_Administrator')")
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateMaintenanceCost(@RequestParam int buildingId) {
        try {
            BigDecimal cost = maintenanceService.calculateMaintenanceCost(buildingId);
            return ResponseEntity.ok(cost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}