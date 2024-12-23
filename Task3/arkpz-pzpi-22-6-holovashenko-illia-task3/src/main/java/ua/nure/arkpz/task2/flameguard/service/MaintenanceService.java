package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.dto.MaintenanceDto;
import ua.nure.arkpz.task2.flameguard.dto.SensorDto;
import ua.nure.arkpz.task2.flameguard.entity.Building;
import ua.nure.arkpz.task2.flameguard.entity.Maintenance;
import ua.nure.arkpz.task2.flameguard.entity.Sensor;
import ua.nure.arkpz.task2.flameguard.entity.SystemSettings;
import ua.nure.arkpz.task2.flameguard.entity.SensorSettings;
import ua.nure.arkpz.task2.flameguard.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private SystemSettingsRepository systemSettingsRepository;
    @Autowired
    private SensorSettingsRepository sensorSettingsRepository;

    // Retrieve all maintenances
    public List<MaintenanceDto> getAllMaintenances() {
        return maintenanceRepository.findAll().stream()
                .map(this::convertToMaintenanceDto)
                .toList();
    }

    // Retrieve a specific maintenance by ID
    public Optional<MaintenanceDto> getMaintenanceById(int id) {
        return maintenanceRepository.findById(id)
                .map(this::convertToMaintenanceDto);
    }

    // Retrieve all maintenances for a specific building
    public List<MaintenanceDto> getMaintenancesByBuildingId(int buildingId) {
        return maintenanceRepository.findMaintenanceByBuilding_BuildingId(buildingId)
                .stream()
                .map(this::convertToMaintenanceDto)
                .toList();
    }

    // Create a new maintenance
    public Optional<MaintenanceDto> createMaintenance(MaintenanceDto maintenanceDto) {

        Optional<Building> building = maintenanceDto.getBuildingId() != null
                ? buildingRepository.findById(maintenanceDto.getBuildingId())
                : Optional.empty();

        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenanceId(maintenanceDto.getMaintenanceId());
        maintenance.setDatePerformed(maintenanceDto.getDatePerformed());
        maintenance.setWorkDescription(maintenanceDto.getWorkDescription());
        if (maintenanceDto.getCost() != null) {
            maintenance.setCost(maintenanceDto.getCost());
        } else {
            building.ifPresent(b -> maintenance.setCost(calculateMaintenanceCost(b.getBuildingId())));
        }
        building.ifPresent(maintenance::setBuilding);

        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
        return Optional.of(convertToMaintenanceDto(savedMaintenance));
    }

    // Update a maintenance
    public Optional<MaintenanceDto> updateMaintenance(int id, MaintenanceDto updatedMaintenanceDto) {
        return maintenanceRepository.findById(id)
                .map(maintenance -> {
                    maintenance.setDatePerformed(updatedMaintenanceDto.getDatePerformed());
                    maintenance.setWorkDescription(updatedMaintenanceDto.getWorkDescription());
                    maintenance.setCost(updatedMaintenanceDto.getCost());

                    if (updatedMaintenanceDto.getBuildingId() != null) {
                        Optional<Building> building = buildingRepository
                                .findById(updatedMaintenanceDto.getBuildingId());
                        building.ifPresent(maintenance::setBuilding);
                    } else {
                        maintenance.setBuilding(null);
                    }

                    Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
                    return convertToMaintenanceDto(savedMaintenance);
        });
    }

    // Delete a maintenance
    public boolean deleteMaintenance(Integer id) {
        if (maintenanceRepository.existsById(id)) {
            maintenanceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public BigDecimal calculateMaintenanceCost(int buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building with ID " + buildingId + " does not exist."));

        BigDecimal basePrice = getBasePriceForBuildingType(building.getBuildingType());
        BigDecimal totalSensorCost = calculateTotalSensorCost(buildingId);

        return basePrice.add(totalSensorCost);
    }

    private BigDecimal getBasePriceForBuildingType(String buildingType) {
        String baseCostKey = "BaseCost_" + buildingType.replace(" ", "");
        return systemSettingsRepository.findBySettingKey(baseCostKey)
                .map(setting -> new BigDecimal(setting.getSettingValue()))
                .orElseThrow(() -> new IllegalStateException("Base cost setting not found for building type: " + buildingType));
    }

    private BigDecimal calculateTotalSensorCost(int buildingId) {
        List<SensorDto> sensors = sensorService.getSensorsByBuildingId(buildingId);

        return sensors.stream()
                .map(sensor -> getServiceCostForSensor(sensor.getSensorId()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getServiceCostForSensor(Integer sensorId) {
        return sensorSettingsRepository.findBySensor_SensorId(sensorId)
                .map(SensorSettings::getServiceCost)
                .orElseThrow(() -> new IllegalStateException("Service cost not found for sensor ID: " + sensorId));
    }

    private MaintenanceDto convertToMaintenanceDto(Maintenance maintenance) {
        return new MaintenanceDto(
                maintenance.getMaintenanceId(),
                maintenance.getDatePerformed(),
                maintenance.getWorkDescription(),
                maintenance.getCost(),
                maintenance.getBuilding() != null ? maintenance.getBuilding().getBuildingId() : null
        );
    }
}
