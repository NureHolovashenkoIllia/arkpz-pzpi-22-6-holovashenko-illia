package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.entity.Building;
import ua.nure.arkpz.task2.flameguard.repository.BuildingRepository;

import java.util.List;

@Service
public class BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    public Building getBuildingById(Integer id) {
        return buildingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + id));
    }

    public List<Building> getBuildingsByUser(Integer userAccountId) {
        return buildingRepository.findByUserAccount_UserAccountId(userAccountId);
    }

    public Building createBuilding(Building building) {
        return buildingRepository.save(building);
    }

    public Building updateBuilding(Integer id, Building updatedBuilding) {
        Building existingBuilding = getBuildingById(id);
        existingBuilding.setBuildingName(updatedBuilding.getBuildingName());
        existingBuilding.setBuildingDescription(updatedBuilding.getBuildingDescription());
        existingBuilding.setCreationDate(updatedBuilding.getCreationDate());
        return buildingRepository.save(existingBuilding);
    }

    public void deleteBuilding(Integer id) {
        Building building = getBuildingById(id);
        buildingRepository.delete(building);
    }

    public void deleteAllBuildings() {
        buildingRepository.deleteAll();
    }
}
