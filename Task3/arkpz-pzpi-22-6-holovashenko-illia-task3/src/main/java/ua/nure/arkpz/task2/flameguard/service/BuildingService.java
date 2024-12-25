package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.dto.BuildingDto;
import ua.nure.arkpz.task2.flameguard.dto.SensorDto;
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

    public List<BuildingDto> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .map(this::convertToBuildingDto)
                .toList();
    }

    public Optional<BuildingDto> getBuildingById(Integer id) {
        return buildingRepository.findById(id)
                .map(this::convertToBuildingDto);
    }

    public List<BuildingDto> getBuildingsByUser(Integer userAccountId) {
        return buildingRepository.findByUserAccount_UserAccountId(userAccountId)
                .stream()
                .map(this::convertToBuildingDto)
                .toList();
    }

    public Optional<BuildingDto> createBuilding(BuildingDto buildingDto) {
        validateBuildingDto(buildingDto);

        Optional<UserAccount> userAccount = buildingDto.getUserAccountId() != null
                ? userAccountRepository.findById(buildingDto.getUserAccountId())
                : Optional.empty();

        Building building = new Building();
        building.setBuildingName(buildingDto.getBuildingName());
        building.setBuildingDescription(buildingDto.getBuildingDescription());
        building.setBuildingType(buildingDto.getBuildingType());
        building.setBuildingCondition(buildingDto.getBuildingCondition());
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
                    building.setBuildingType(updatedBuilding.getBuildingType());
                    building.setBuildingCondition(updatedBuilding.getBuildingCondition());
                    building.setCreationDate(updatedBuilding.getCreationDate());

                    if (updatedBuilding.getUserAccountId() != null) {
                        Optional<UserAccount> user = userAccountRepository
                                .findById(updatedBuilding.getUserAccountId());
                        user.ifPresent(building::setUserAccount);
                    } else {
                        building.setUserAccount(null);
                    }

                    validateBuildingDto(convertToBuildingDto(building));

                    Building savedBuilding = buildingRepository.save(building);
                    return convertToBuildingDto(savedBuilding);
                });
    }

    public Optional<BuildingDto> updateBuildingCondition(Integer id, String updatedCondition) {
        return buildingRepository.findById(id)
                .map(building -> {
                    building.setBuildingCondition(updatedCondition);

                    validateBuildingDto(convertToBuildingDto(building));

                    Building updatedBuilding = buildingRepository.save(building);
                    return convertToBuildingDto(updatedBuilding);
                });
    }

    public boolean deleteBuilding(Integer id) {
        if (buildingRepository.existsById(id)) {
            buildingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Validate building fields
    private void validateBuildingDto(BuildingDto buildingDto) {
        if (!buildingDto.getBuildingCondition().matches("Excellent|Good|Fair|Poor|Dangerous")) {
            throw new IllegalArgumentException("Invalid building condition: " + buildingDto.getBuildingCondition());
        }
    }

    private BuildingDto convertToBuildingDto(Building building) {
        return new BuildingDto(
                building.getBuildingId(),
                building.getBuildingName(),
                building.getBuildingDescription(),
                building.getBuildingType(),
                building.getBuildingCondition(),
                building.getCreationDate(),
                building.getUserAccount() != null ? building.getUserAccount().getUserAccountId() : null
        );
    }
}
