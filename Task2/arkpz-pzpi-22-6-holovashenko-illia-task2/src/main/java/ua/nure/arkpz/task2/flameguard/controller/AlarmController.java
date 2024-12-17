package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.AlarmDto;
import ua.nure.arkpz.task2.flameguard.dto.SensorDto;
import ua.nure.arkpz.task2.flameguard.entity.Alarm;
import ua.nure.arkpz.task2.flameguard.service.AlarmService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    // Retrieve all alarms
    @GetMapping
    public ResponseEntity<List<AlarmDto>> getAllAlarms() {
        List<AlarmDto> alarms = alarmService.getAllAlarms();
        return ResponseEntity.ok(alarms);
    }

    // Retrieve an alarm by ID
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

    // Retrieve all alarms for a specific sensor
    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<?> getAlarmsBySensorId(@PathVariable int sensorId) {
        List<AlarmDto> alarms = alarmService.getAlarmsBySensorId(sensorId);

        if (alarms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No alarms found for sensor with id - " + sensorId + "\"}");
        }
        return ResponseEntity.ok(alarms);
    }

    // Retrieve all alarms for a specific building
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<?> getAlarmsByBuildingId(@PathVariable int buildingId) {
        List<AlarmDto> alarms = alarmService.getAlarmsByBuildingId(buildingId);

        if (alarms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No alarms found for building with id - " + buildingId + "\"}");
        }
        return ResponseEntity.ok(alarms);
    }

    // Filter alarms by type
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getAlarmsByType(@PathVariable String type) {
        List<AlarmDto> alarms = alarmService.getAlarmsByType(type);

        if (alarms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No alarms found with type - " + type + "\"}");
        }
        return ResponseEntity.ok(alarms);
    }

    // Create a new alarm
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

    // Update an existing alarm
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

    // Delete an alarm
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
}
