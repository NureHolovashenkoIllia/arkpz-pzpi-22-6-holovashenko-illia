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
import ua.nure.arkpz.task2.flameguard.dto.SensorDto;
import ua.nure.arkpz.task2.flameguard.service.NotificationService;
import ua.nure.arkpz.task2.flameguard.service.SensorService;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing sensors.
 * Provides endpoints for CRUD operations on sensors.
 */
@RestController
@RequestMapping("/api/sensors")
@Tag(name = "Sensors", description = "Endpoints for managing sensors")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private NotificationService notificationService;
    /**
     * Retrieve all sensors.
     *
     * @return List of SensorDto objects.
     */
    @Operation(summary = "Get all sensors", description = "Retrieve a list of all sensors.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensors.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SensorDto.class)))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @GetMapping
    public ResponseEntity<List<SensorDto>> getAllSensors() {
        List<SensorDto> sensors = sensorService.getAllSensors();
        return ResponseEntity.ok(sensors);
    }

    /**
     * Retrieve a sensor by its ID.
     *
     * @param id ID of the sensor.
     * @return SensorDto object if found, or error message if not found.
     */
    @Operation(summary = "Get sensor by ID", description = "Retrieve a specific sensor by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SensorDto.class))),
            @ApiResponse(responseCode = "404", description = "Sensor not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No sensor found with specified id\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getSensorById(@PathVariable int id) {
        Optional<SensorDto> sensor = sensorService.getSensorById(id);

        if (sensor.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(sensor);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\":\"No sensor found with id - " + id + "\"}");
    }

    /**
     * Retrieve all sensors with status "Enabled".
     *
     * @return List of SensorDto objects with status "Enabled".
     */
    @Operation(summary = "Get all enabled sensors", description = "Retrieve a list of all sensors with status 'Enabled'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved enabled sensors.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SensorDto.class)))
    })
    @GetMapping("/enabled")
    public ResponseEntity<List<SensorDto>> getEnabledSensors() {
        List<SensorDto> sensors = sensorService.getEnabledSensors();
        return ResponseEntity.ok(sensors);
    }

    /**
     * Retrieve sensors by building ID.
     *
     * @param buildingId ID of the building.
     * @return List of sensors associated with the building, or error message if none are found.
     */
    @Operation(summary = "Get sensors by building ID", description = "Retrieve sensors associated with a specific building.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensors found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SensorDto.class))),
            @ApiResponse(responseCode = "404", description = "No sensors found for the building.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No sensors found for building with specified id\"}")))
    })
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<?> getSensorsByBuildingId(@PathVariable int buildingId) {
        List<SensorDto> sensors = sensorService.getSensorsByBuildingId(buildingId);

        if (sensors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No sensors found for building with id - " + buildingId + "\"}");
        }
        return ResponseEntity.ok(sensors);
    }

    /**
     * Create a new sensor.
     *
     * @param sensorDto DTO representing the sensor to create.
     * @return Created SensorDto object or error message if creation fails.
     */
    @Operation(summary = "Create a new sensor", description = "Add a new sensor to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sensor created successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SensorDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or creation failed.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"Invalid sensor data\"}")))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @PostMapping
    public ResponseEntity<?> createSensor(@RequestBody SensorDto sensorDto) {
        try {
            Optional<SensorDto> createdSensor = sensorService.createSensor(sensorDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdSensor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Update sensor details.
     *
     * @param id ID of the sensor to update.
     * @param updatedSensorDto DTO containing updated sensor details.
     * @return Updated SensorDto object or error message if update fails.
     */
    @Operation(summary = "Update a sensor", description = "Update the details of an existing sensor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor updated successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SensorDto.class))),
            @ApiResponse(responseCode = "404", description = "Sensor not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No sensor found with specified id\"}")))
    })
    @PreAuthorize("hasAnyAuthority('Customer')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSensor(@PathVariable int id,
                                          @RequestBody SensorDto updatedSensorDto) {
        try {
            Optional<SensorDto> updatedSensor =
                    sensorService.updateSensor(id, updatedSensorDto);
            return ResponseEntity.ok(updatedSensor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Delete a sensor by ID.
     *
     * @param id ID of the sensor to delete.
     * @return HTTP status indicating the result of the operation.
     */
    @Operation(summary = "Delete a sensor", description = "Remove a sensor from the system by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Sensor not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No sensor found with specified id\"}")))
    })
    @PreAuthorize("hasAuthority('Global_Administrator')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSensor(@PathVariable int id) {
        try {
            sensorService.deleteSensor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Update the building ID associated with a sensor.
     *
     * @param id         ID of the sensor to update.
     * @param buildingId New building ID to associate with the sensor (can be null).
     * @return Updated SensorDto object or error message if update fails.
     */
    @Operation(summary = "Update sensor's building", description = "Associate or disassociate a sensor with a building.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor updated successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SensorDto.class))),
            @ApiResponse(responseCode = "404", description = "Sensor not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No sensor found with specified id\"}")))
    })
    @PreAuthorize("hasAuthority('Global_Administrator')")
    @PatchMapping("/{id}/building")
    public ResponseEntity<?> updateSensorBuilding(@PathVariable int id,
                                                  @RequestParam(required = false) Integer buildingId) {
        try {
            Optional<SensorDto> updatedSensor = sensorService.updateSensorBuilding(id, buildingId);
            return ResponseEntity.ok(updatedSensor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Update the status of a sensor.
     *
     * @param id     ID of the sensor to update.
     * @param status New status for the sensor (Enabled, Faulty, or Disabled).
     * @return Updated SensorDto object or error message if update fails.
     */
    @Operation(summary = "Update sensor's status", description = "Change the status of a sensor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor status updated successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SensorDto.class))),
            @ApiResponse(responseCode = "404", description = "Sensor not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No sensor found with specified id\"}")))
    })
    @PreAuthorize("hasAuthority('Global_Administrator')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateSensorStatus(@PathVariable int id,
                                                @RequestParam String status) {
        try {
            Optional<SensorDto> updatedSensor = sensorService.updateSensorStatus(id, status);
            return ResponseEntity.ok(updatedSensor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/notify")
    public ResponseEntity<String> sendWarningSensorStatusNotification(@RequestParam Integer sensorId) {
        try {
            notificationService.sendSensorStatusNotification(sensorId);
            return new ResponseEntity<>("Notification sent successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send notification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
