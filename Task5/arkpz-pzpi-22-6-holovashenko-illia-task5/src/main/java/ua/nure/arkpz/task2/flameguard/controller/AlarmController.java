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
import ua.nure.arkpz.task2.flameguard.dto.AlarmDto;
import ua.nure.arkpz.task2.flameguard.service.AlarmService;
import ua.nure.arkpz.task2.flameguard.service.NotificationService;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing alarms.
 * Provides endpoints for CRUD operations on alarms and filtering by different criteria.
 */
@RestController
@RequestMapping("/api/alarms")
@Tag(name = "Alarms", description = "Endpoints for managing alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;
    @Autowired
    private NotificationService notificationService;

    /**
     * Retrieves all alarms.
     *
     * @return a list of all alarms in the system.
     */
    @Operation(summary = "Retrieve all alarms", description = "Returns a list of all alarms in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of alarms",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmDto.class)))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @GetMapping
    public ResponseEntity<List<AlarmDto>> getAllAlarms() {
        List<AlarmDto> alarms = alarmService.getAllAlarms();
        return ResponseEntity.ok(alarms);
    }

    /**
     * Retrieves an alarm by its ID.
     *
     * @param id the ID of the alarm.
     * @return the alarm details if found, or a 404 error if not found.
     */
    @Operation(summary = "Retrieve an alarm by ID", description = "Returns the details of an alarm specified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the alarm",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmDto.class))),
            @ApiResponse(responseCode = "404", description = "Alarm not found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getAlarmById(@PathVariable int id) {
        Optional<AlarmDto> alarm = alarmService.getAlarmById(id);

        if (alarm.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(alarm);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\":\"No alarm found with id - " + id + "\"}");
    }

    /**
     * Retrieves all alarms for a specific sensor.
     *
     * @param sensorId the ID of the sensor.
     * @return a list of alarms associated with the specified sensor.
     */
    @Operation(summary = "Retrieve alarms by sensor ID", description = "Returns all alarms associated with the specified sensor ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved alarms for the sensor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmDto.class))),
            @ApiResponse(responseCode = "404", description = "No alarms found for the specified sensor ID",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<?> getAlarmsBySensorId(@PathVariable int sensorId) {
        List<AlarmDto> alarms = alarmService.getAlarmsBySensorId(sensorId);

        if (alarms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No alarms found for sensor with id - " + sensorId + "\"}");
        }
        return ResponseEntity.ok(alarms);
    }

    /**
     * Retrieves all alarms for a specific building.
     *
     * @param buildingId the ID of the building.
     * @return a list of alarms associated with the specified building.
     */
    @Operation(summary = "Retrieve alarms by building ID", description = "Returns all alarms associated with the specified building ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved alarms for the building",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmDto.class))),
            @ApiResponse(responseCode = "404", description = "No alarms found for the specified building ID",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<?> getAlarmsByBuildingId(@PathVariable int buildingId) {
        List<AlarmDto> alarms = alarmService.getAlarmsByBuildingId(buildingId);

        if (alarms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No alarms found for building with id - " + buildingId + "\"}");
        }
        return ResponseEntity.ok(alarms);
    }

    /**
     * Filters alarms by type.
     *
     * @param type the type of the alarms (e.g., "Fire alarm", "System Failure").
     * @return a list of alarms with the specified type.
     */
    @Operation(summary = "Filter alarms by type", description = "Returns a list of alarms filtered by their type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved alarms of the specified type",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmDto.class))),
            @ApiResponse(responseCode = "404", description = "No alarms found with the specified type",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getAlarmsByType(@PathVariable String type) {
        List<AlarmDto> alarms = alarmService.getAlarmsByType(type);

        if (alarms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No alarms found with type - " + type + "\"}");
        }
        return ResponseEntity.ok(alarms);
    }

    /**
     * Creates a new alarm.
     *
     * @param alarmDto the details of the new alarm to be created.
     * @return the created alarm.
     */
    @Operation(summary = "Create a new alarm", description = "Creates a new alarm in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the alarm",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @PostMapping
    public ResponseEntity<?> createAlarm(@RequestBody AlarmDto alarmDto) {
        try {
            Optional<AlarmDto> createdAlarm = alarmService.createAlarm(alarmDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdAlarm);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Updates an existing alarm by its ID.
     *
     * @param id              the ID of the alarm to update.
     * @param updatedAlarmDto the updated alarm details.
     * @return the updated alarm details.
     */
    @Operation(summary = "Update an alarm", description = "Updates an existing alarm in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the alarm",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmDto.class))),
            @ApiResponse(responseCode = "404", description = "Alarm not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlarm(@PathVariable int id,
                                         @RequestBody AlarmDto updatedAlarmDto) {
        try {
            Optional<AlarmDto> updatedAlarm = alarmService.updateAlarm(id, updatedAlarmDto);
            return ResponseEntity.ok(updatedAlarm);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Deletes an alarm by its ID.
     *
     * @param id the ID of the alarm to delete.
     * @return a no-content response if successful.
     */
    @Operation(summary = "Delete an alarm", description = "Deletes an alarm specified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the alarm"),
            @ApiResponse(responseCode = "404", description = "Alarm not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasAuthority('Global_Administrator')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlarm(@PathVariable int id) {
        try {
            alarmService.deleteAlarm(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/notify")
    public ResponseEntity<String> sendNotification(@RequestParam Integer alarmId) {
        try {
            notificationService.sendAlarmNotification(alarmId);
            return new ResponseEntity<>("Notification sent successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send notification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
