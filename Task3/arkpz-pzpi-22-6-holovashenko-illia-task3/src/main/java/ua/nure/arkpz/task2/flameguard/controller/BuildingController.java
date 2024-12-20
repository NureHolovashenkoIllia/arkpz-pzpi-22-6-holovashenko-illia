package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.BuildingDto;
import ua.nure.arkpz.task2.flameguard.service.BuildingService;

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
    public ResponseEntity<List<BuildingDto>> getAllBuildings() {
        List<BuildingDto> buildings = buildingService.getAllBuildings();
        return ResponseEntity.ok(buildings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBuildingById(@PathVariable Integer id) {
        Optional<BuildingDto> building = buildingService.getBuildingById(id);

        if (building.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(building);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\":\"No building found with id - " + id + "\"}");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBuildingByUserId(@PathVariable Integer userId) {
        List<BuildingDto> buildings = buildingService.getBuildingsByUser(userId);

        if (buildings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No buildings found for user with id - " + userId + "\"}");
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
    public ResponseEntity<?> deleteBuilding(@PathVariable Integer id) {
        try {
            buildingService.deleteBuilding(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
