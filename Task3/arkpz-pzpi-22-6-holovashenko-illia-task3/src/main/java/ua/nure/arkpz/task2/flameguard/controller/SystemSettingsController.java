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
import ua.nure.arkpz.task2.flameguard.dto.AddressDto;
import ua.nure.arkpz.task2.flameguard.entity.SystemSettings;
import ua.nure.arkpz.task2.flameguard.service.SystemSettingsService;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing system settings.
 * Provides endpoints for view and edit settings for administrators.
 */
@RestController
@RequestMapping("/api/admin/system-settings")
@Tag(name = "System Settings", description = "Endpoints for view and edit settings for administrators")
public class SystemSettingsController {

    @Autowired
    private SystemSettingsService systemSettingsService;

    /**
     * Retrieves all system settings.
     *
     * @return a list of all system settings.
     */
    @Operation(summary = "Get all system settings", description = "Fetches all available system settings.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Settings retrieved successfully.",
                    content = @Content(schema = @Schema(implementation = SystemSettings.class))),
            @ApiResponse(responseCode = "403", description = "Access is denied.",
                    content = @Content(schema = @Schema(type = "string", example = "{\"error\":\"Access Denied\"}")))
    })
    @PreAuthorize("hasAuthority('System_Administrator')")
    @GetMapping
    public ResponseEntity<List<SystemSettings>> getAllSettings() {
        List<SystemSettings> settings = systemSettingsService.getAllSettings();
        return ResponseEntity.ok(settings);
    }

    /**
     * Updates the value of a specific system setting.
     *
     * @param key   the key of the setting to update.
     * @param value the new value to set.
     * @return the updated system setting or an error message if the setting is not found.
     */
    @Operation(summary = "Update a system setting", description = "Updates the value of a specific system setting identified by its key.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Setting updated successfully.",
                    content = @Content(schema = @Schema(implementation = SystemSettings.class))),
            @ApiResponse(responseCode = "404", description = "Setting not found.",
                    content = @Content(schema = @Schema(type = "string", example = "{\"error\":\"Setting not found\"}"))),
            @ApiResponse(responseCode = "403", description = "Access is denied.",
                    content = @Content(schema = @Schema(type = "string", example = "{\"error\":\"Access Denied\"}")))
    })
    @PreAuthorize("hasAuthority('System_Administrator')")
    @PatchMapping("/{key}")
    public ResponseEntity<?> updateSettingValue(@PathVariable String key, @RequestParam String value) {
        try {
            Optional<SystemSettings> updatedSystemSettings =
                    systemSettingsService.updateSettingValue(key, value);
            return ResponseEntity.ok(updatedSystemSettings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}