package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.MaintenanceDto;
import ua.nure.arkpz.task2.flameguard.service.MaintenanceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/maintenances")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    // Retrieve all maintenances
    @GetMapping
    public ResponseEntity<List<MaintenanceDto>> getAllMaintenances() {
        List<MaintenanceDto> maintenances = maintenanceService.getAllMaintenances();
        return ResponseEntity.ok(maintenances);
    }

    // Retrieve a specific maintenance by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMaintenanceById(@PathVariable int id) {
        Optional<MaintenanceDto> maintenance = maintenanceService.getMaintenanceById(id);

        if (maintenance.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(maintenance);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\":\"No sensor found with id - " + id + "\"}");
    }

    // Retrieve all maintenances for a specific building
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

    // Create a new maintenance
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

    // Update a maintenance
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

    // Delete a maintenance
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
}