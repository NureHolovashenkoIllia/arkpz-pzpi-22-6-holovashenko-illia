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
import ua.nure.arkpz.task2.flameguard.entity.SensorSettings;
import ua.nure.arkpz.task2.flameguard.entity.UserAccount;
import ua.nure.arkpz.task2.flameguard.service.SensorSettingsService;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * REST controller for managing sensor settings.
 * Provides endpoints for editing sensor settings for administrators.
 */
@RestController
@RequestMapping("/api/admin/sensor-settings" )
@Tag(name = "Sensor Settings", description = "Endpoints for editing sensor settings for administrators")
public class SensorSettingsController {

    @Autowired
    private SensorSettingsService sensorSettingsService;

    /**
     * Updates the critical value of a sensor setting.
     *
     * @param id           the ID of the sensor setting to update.
     * @param criticalValue the new critical value to set.
     * @return a response entity with the updated sensor settings or an error message if not found.
     */
    @Operation(summary = "Update critical value", description = "Updates the critical value of the specified sensor setting.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Critical value updated successfully.",
                    content = @Content(schema = @Schema(implementation = SensorSettings.class))),
            @ApiResponse(responseCode = "404", description = "Sensor settings not found.",
                    content = @Content(schema = @Schema(type = "string", example = "{\"error\":\"Not Found\"}")))
    })
    @PreAuthorize("hasAuthority('System_Administrator')")
    @PatchMapping("/{id}/critical-value")
    public ResponseEntity<?> updateCriticalValue(@PathVariable Integer id, @RequestParam Float criticalValue) {
        try {
            Optional<SensorSettings> updatedSensorSettings = sensorSettingsService.updateCriticalValue(id, criticalValue);
            return ResponseEntity.ok(updatedSensorSettings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Updates the measurement frequency of a sensor setting.
     *
     * @param id                  the ID of the sensor setting to update.
     * @param measurementFrequency the new measurement frequency to set.
     * @return a response entity with the updated sensor settings or an error message if not found.
     */
    @Operation(summary = "Update measurement frequency", description = "Updates the measurement frequency of the specified sensor setting.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Measurement frequency updated successfully.",
                    content = @Content(schema = @Schema(implementation = SensorSettings.class))),
            @ApiResponse(responseCode = "404", description = "Sensor settings not found.",
                    content = @Content(schema = @Schema(type = "string", example = "{\"error\":\"Not Found\"}")))
    })
    @PreAuthorize("hasAuthority('System_Administrator')")
    @PatchMapping("/{id}/measurement-frequency")
    public ResponseEntity<?> updateMeasurementFrequency(@PathVariable Integer id, @RequestParam Integer measurementFrequency) {
        try {
            Optional<SensorSettings> updatedSensorSettings = sensorSettingsService.updateMeasurementFrequency(id, measurementFrequency);
            return ResponseEntity.ok(updatedSensorSettings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Updates the service cost of a sensor setting.
     *
     * @param id         the ID of the sensor setting to update.
     * @param serviceCost the new service cost to set.
     * @return a response entity with the updated sensor settings or an error message if not found.
     */
    @Operation(summary = "Update service cost", description = "Updates the service cost of the specified sensor setting.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service cost updated successfully.",
                    content = @Content(schema = @Schema(implementation = SensorSettings.class))),
            @ApiResponse(responseCode = "404", description = "Sensor settings not found.",
                    content = @Content(schema = @Schema(type = "string", example = "{\"error\":\"Not Found\"}")))
    })
    @PreAuthorize("hasAuthority('System_Administrator')")
    @PatchMapping("/{id}/service-cost")
    public ResponseEntity<?> updateServiceCost(@PathVariable Integer id, @RequestParam BigDecimal serviceCost) {
        try {
            Optional<SensorSettings> updatedSensorSettings = sensorSettingsService.updateServiceCost(id, serviceCost);
            return ResponseEntity.ok(updatedSensorSettings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}