package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.entity.Building;
import ua.nure.arkpz.task2.flameguard.service.BuildingService;
import ua.nure.arkpz.task2.flameguard.service.UserAccountService;

import java.util.List;

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
    public ResponseEntity<List<Building>> getBuildingByUserId(@PathVariable Integer userId) {
        List<Building> buildings = buildingService.getBuildingsByUser(userId);
        return ResponseEntity.ok(buildings);
    }

    @PostMapping
    public ResponseEntity<Building> createBuilding(@RequestBody Building building) {
        return ResponseEntity.ok(buildingService.createBuilding(building));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Building> updateBuilding(@PathVariable Integer id, @RequestBody Building updatedBuilding) {
        return ResponseEntity.ok(buildingService.updateBuilding(id, updatedBuilding));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Integer id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllBuildings() {
        buildingService.deleteAllBuildings();
        return ResponseEntity.noContent().build();
    }
}
