package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.BuildingDto;
import ua.nure.arkpz.task2.flameguard.entity.Building;
import ua.nure.arkpz.task2.flameguard.service.BuildingService;
import ua.nure.arkpz.task2.flameguard.service.UserAccountService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @Autowired
    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping
    public List<Building> getAllBuildings() {
        return buildingService.getAllBuildings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Integer id) {
        return ResponseEntity.ok(buildingService.getBuildingById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBuildingByUserId(@PathVariable Integer userId) {
        List<BuildingDto> buildings = buildingService.getBuildingsByUser(userId);

        if (buildings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"No buildings found for user with id - " + userId + "\"}");
        }
        return ResponseEntity.ok(buildings);
    }

    @PostMapping
    public ResponseEntity<?> createBuilding(@RequestBody BuildingDto buildingDto) {
        try {
            Optional<BuildingDto> createdBuilding = buildingService.createBuilding(buildingDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdBuilding);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBuilding(@PathVariable Integer id,
                                                   @RequestBody BuildingDto buildingDto) {
        try {
            Optional<BuildingDto> updatedBuilding = buildingService.updateBuilding(id, buildingDto);
            return ResponseEntity.ok(updatedBuilding);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Integer id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }
}
