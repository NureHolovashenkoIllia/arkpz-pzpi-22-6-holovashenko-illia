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
import ua.nure.arkpz.task2.flameguard.dto.MeasurementDto;
import ua.nure.arkpz.task2.flameguard.service.MeasurementService;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing measurements.
 * Provides endpoints for Create, Read, Delete operations on measurements.
 */
@RestController
@RequestMapping("/api/measurements")
@Tag(name = "Measurements", description = "Endpoints for managing measurements")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    /**
     * Retrieve all measurements.
     *
     * @return List of MeasurementDto objects.
     */
    @Operation(summary = "Get all measurements", description = "Retrieve a list of all measurements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved measurements.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasurementDto.class)))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @GetMapping
    public ResponseEntity<List<MeasurementDto>> getAllMeasurements() {
        List<MeasurementDto> measurements = measurementService.getAllMeasurements();
        return ResponseEntity.ok(measurements);
    }

    /**
     * Retrieve a specific measurement by its ID.
     *
     * @param id ID of the measurement.
     * @return MeasurementDto object if found, or error message if not found.
     */
    @Operation(summary = "Get measurement by ID", description = "Retrieve a specific measurement by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurement found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasurementDto.class))),
            @ApiResponse(responseCode = "404", description = "Measurement not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No measurement found with specified id\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getMeasurementById(@PathVariable int id) {
        Optional<MeasurementDto> measurement = measurementService.getMeasurementById(id);

        if (measurement.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(measurement);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\":\"No measurement found with id - " + id + "\"}");
    }

    /**
     * Retrieve measurements by sensor ID.
     *
     * @param sensorId ID of the sensor.
     * @return List of measurements associated with the sensor, or error message if none are found.
     */
    @Operation(summary = "Get measurements by sensor ID", description = "Retrieve measurements associated with a specific sensor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurements found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasurementDto.class))),
            @ApiResponse(responseCode = "404", description = "No measurements found for the sensor.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No measurements found for sensor with specified id\"}")))
    })
    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<?> getMeasurementsBySensorId(@PathVariable int sensorId) {
        List<MeasurementDto> measurements = measurementService.getMeasurementsBySensorId(sensorId);

        if (measurements.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No measurements found for sensor with id - " + sensorId + "\"}");
        }
        return ResponseEntity.ok(measurements);
    }

    /**
     * Filter measurements by unit for a specific sensor.
     *
     * @param sensorId ID of the sensor.
     * @param unit     Unit to filter measurements by.
     * @return List of filtered measurements, or error message if none are found.
     */
    @Operation(summary = "Filter measurements by unit", description = "Filter measurements by unit for a specific sensor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered measurements found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasurementDto.class))),
            @ApiResponse(responseCode = "404", description = "No measurements found with the specified unit.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No measurements found for sensor with specified id and unit\"}")))
    })
    @GetMapping("/sensor/{sensorId}/unit")
    public ResponseEntity<?> filterMeasurementsByUnit(@PathVariable int sensorId, @RequestParam String unit) {
        List<MeasurementDto> measurements = measurementService.filterMeasurementsByUnit(sensorId, unit);

        if (measurements.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No measurements found for sensor with id - " + sensorId +
                            " with unit - " + unit + "\"}");
        }
        return ResponseEntity.ok(measurements);
    }

    /**
     * Create a new measurement.
     *
     * @param measurementDto DTO representing the measurement to create.
     * @return Created MeasurementDto object or error message if creation fails.
     */
    @Operation(summary = "Create a new measurement", description = "Add a new measurement to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Measurement created successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasurementDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or creation failed.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"Invalid measurement data\"}")))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @PostMapping
    public ResponseEntity<?> createMeasurement(@RequestBody MeasurementDto measurementDto) {
        try {
            Optional<MeasurementDto> createdMeasurement = measurementService.createMeasurement(measurementDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdMeasurement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Delete a measurement.
     *
     * @param id ID of the measurement to delete.
     * @return HTTP status indicating the result of the operation.
     */
    @Operation(summary = "Delete a measurement", description = "Remove a measurement from the system by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Measurement deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Measurement not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No measurement found with specified id\"}")))
    })
    @PreAuthorize("hasAuthority('Global_Administrator')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeasurement(@PathVariable int id) {
        try {
            measurementService.deleteMeasurement(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}