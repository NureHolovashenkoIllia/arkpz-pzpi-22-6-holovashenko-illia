package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.MeasurementDto;
import ua.nure.arkpz.task2.flameguard.dto.SensorDto;
import ua.nure.arkpz.task2.flameguard.entity.Measurement;
import ua.nure.arkpz.task2.flameguard.service.MeasurementService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    // Retrieve all measurements
    @GetMapping
    public ResponseEntity<List<MeasurementDto>> getAllMeasurements() {
        List<MeasurementDto> measurements = measurementService.getAllMeasurements();
        return ResponseEntity.ok(measurements);
    }

    // Retrieve a specific measurement by ID
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

    // Retrieve measurements by sensor ID
    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<?> getMeasurementsBySensorId(@PathVariable int sensorId) {
        List<MeasurementDto> measurements = measurementService.getMeasurementsBySensorId(sensorId);

        if (measurements.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No measurements found for sensor with id - " + sensorId + "\"}");
        }
        return ResponseEntity.ok(measurements);
    }

    // Filter measurements by unit for a specific sensor
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

    // Create a new measurement
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

    // Delete a measurement
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