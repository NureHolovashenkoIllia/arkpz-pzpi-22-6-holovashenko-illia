package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.SensorDto;
import ua.nure.arkpz.task2.flameguard.service.SensorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @GetMapping
    public ResponseEntity<List<SensorDto>> getAllSensors() {
        List<SensorDto> sensors = sensorService.getAllSensors();
        return ResponseEntity.ok(sensors);
    }

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

    @GetMapping("/building/{buildingId}")
    public ResponseEntity<?> getSensorsByBuildingId(@PathVariable int buildingId) {
        List<SensorDto> sensors = sensorService.getSensorsByBuildingId(buildingId);

        if (sensors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No sensors found for building with id - " + buildingId + "\"}");
        }
        return ResponseEntity.ok(sensors);
    }

    // Create a new sensor
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

    // Update sensor details
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

    // Delete a sensor
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

    // Update building_id field (can set it to null)
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

    // Change sensor status (Enabled|Faulty|Disabled)
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
}
