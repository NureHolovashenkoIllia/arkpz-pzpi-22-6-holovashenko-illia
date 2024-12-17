package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.dto.BuildingDto;
import ua.nure.arkpz.task2.flameguard.entity.Building;
import ua.nure.arkpz.task2.flameguard.entity.UserAccount;
import ua.nure.arkpz.task2.flameguard.repository.BuildingRepository;
import ua.nure.arkpz.task2.flameguard.repository.UserAccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    public Building getBuildingById(Integer id) {
        return buildingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + id));
    }

    public List<BuildingDto> getBuildingsByUser(Integer userAccountId) {
        return buildingRepository.findByUserAccount_UserAccountId(userAccountId)
                .stream()
                .map(this::convertToBuildingDto)
                .toList();
    }

    public Optional<BuildingDto> createBuilding(BuildingDto buildingDto) {
        Optional<UserAccount> userAccount = buildingDto.getUserAccountId() != null
                ? userAccountRepository.findById(buildingDto.getUserAccountId())
                : Optional.empty();

        Building building = new Building();
        building.setBuildingName(buildingDto.getBuildingName());
        building.setBuildingDescription(buildingDto.getBuildingDescription());
        building.setCreationDate(buildingDto.getCreationDate());
        userAccount.ifPresent(building::setUserAccount);

        Building savedBuilding = buildingRepository.save(building);
        return Optional.of(convertToBuildingDto(savedBuilding));
    }

    public Optional<BuildingDto> updateBuilding(Integer id, BuildingDto updatedBuilding) {
        return buildingRepository.findById(id)
                .map(building -> {
                    building.setBuildingName(updatedBuilding.getBuildingName());
                    building.setBuildingDescription(updatedBuilding.getBuildingDescription());
                    building.setCreationDate(updatedBuilding.getCreationDate());

                    if (updatedBuilding.getUserAccountId() != null) {
                        Optional<UserAccount> user = userAccountRepository
                                .findById(updatedBuilding.getUserAccountId());
                        user.ifPresent(building::setUserAccount);
                    } else {
                        building.setUserAccount(null);
                    }

                    Building savedBuilding = buildingRepository.save(building);
                    return convertToBuildingDto(savedBuilding);
                });
    }

    public boolean deleteBuilding(Integer id) {
        if (buildingRepository.existsById(id)) {
            buildingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private BuildingDto convertToBuildingDto(Building building) {
        return new BuildingDto(
                building.getBuildingId(),
                building.getBuildingName(),
                building.getBuildingDescription(),
                building.getCreationDate(),
                building.getUserAccount() != null ? building.getUserAccount().getUserAccountId() : null
        );
    }
}
