package ua.nure.arkpz.task2.flameguard.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.dto.SensorDto;
import ua.nure.arkpz.task2.flameguard.entity.Building;
import ua.nure.arkpz.task2.flameguard.entity.Sensor;
import ua.nure.arkpz.task2.flameguard.repository.BuildingRepository;
import ua.nure.arkpz.task2.flameguard.repository.SensorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    // Retrieve all sensors
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    // Retrieve a specific sensor by ID
    public Optional<Sensor> getSensorById(int id) {
        return sensorRepository.findById(id);
    }

    // Retrieve sensors by building ID
    public List<SensorDto> getSensorsByBuildingId(int buildingId) {
        return sensorRepository.findByBuilding_BuildingId(buildingId)
                .stream()
                .map(this::convertToSensorDto)
                .toList();
    }

    // Create a new sensor
    public Optional<SensorDto> createSensor(SensorDto sensorDto) {
        validateSensorDto(sensorDto);

        Optional<Building> building = sensorDto.getBuildingId() != null
                ? buildingRepository.findById(sensorDto.getBuildingId())
                : Optional.empty();

        Sensor sensor = new Sensor();
        sensor.setSensorName(sensorDto.getSensorName());
        sensor.setSensorType(sensorDto.getSensorType());
        sensor.setSensorStatus(sensorDto.getSensorStatus());
        sensor.setDateAdded(sensorDto.getDateAdded());
        building.ifPresent(sensor::setBuilding);

        Sensor savedSensor = sensorRepository.save(sensor);
        return Optional.of(convertToSensorDto(savedSensor));
    }

    // Update sensor details
    public Optional<SensorDto> updateSensor(Integer id, SensorDto updatedSensorDto) {
        return sensorRepository.findById(id)
                .map(sensor -> {
                    sensor.setSensorName(updatedSensorDto.getSensorName());
                    sensor.setSensorType(updatedSensorDto.getSensorType());
                    sensor.setSensorStatus(updatedSensorDto.getSensorStatus());
                    sensor.setDateAdded(updatedSensorDto.getDateAdded());

                    if (updatedSensorDto.getBuildingId() != null) {
                        Optional<Building> building = buildingRepository
                                .findById(updatedSensorDto.getBuildingId());
                        building.ifPresent(sensor::setBuilding);
                    } else {
                        sensor.setBuilding(null);
                    }

                    validateSensorDto(convertToSensorDto(sensor));

                    Sensor savedSensor = sensorRepository.save(sensor);
                    return convertToSensorDto(savedSensor);
        });
    }

    // Delete a sensor
    public boolean deleteSensor(Integer id) {
        if (sensorRepository.existsById(id)) {
            sensorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Update building_id field (can set it to null)
    public Optional<SensorDto> updateSensorBuilding(int id, Integer buildingId) {
        return sensorRepository.findById(id)
                .map(sensor -> {

                    if (buildingId == null) {
                        sensor.setBuilding(null);
                    } else {
                        if (buildingRepository.findById(buildingId).isEmpty()) {
                            throw new IllegalArgumentException("The specified building id " + buildingId + " does not exist");
                        } else {
                            Building newBuilding = buildingRepository.getReferenceById(buildingId);
                            sensor.setBuilding(newBuilding);
                        }
                    }

                    Sensor savedSensor = sensorRepository.save(sensor);
                    return convertToSensorDto(savedSensor);
        });
    }

    // Change sensor status (Enabled|Faulty|Disabled)
    public Optional<SensorDto> updateSensorStatus(int id, String status) {
        return sensorRepository.findById(id)
                .map(sensor -> {
                    if (!status.matches("Enabled|Faulty|Disabled")) {
                        throw new IllegalArgumentException("Invalid status: " + status);
                    }
                    sensor.setSensorStatus(status);

                    Sensor savedSensor = sensorRepository.save(sensor);
                    return convertToSensorDto(savedSensor);
        });
    }

    // Validate sensor fields
    private void validateSensorDto(SensorDto sensorDto) {
        if (!sensorDto.getSensorType().matches("Temperature|Gas|Smoke|Humidity")) {
            throw new IllegalArgumentException("Invalid sensor type: " + sensorDto.getSensorType());
        }
        if (!sensorDto.getSensorStatus().matches("Enabled|Faulty|Disabled")) {
            throw new IllegalArgumentException("Invalid sensor status: " + sensorDto.getSensorStatus());
        }
    }

    private SensorDto convertToSensorDto(Sensor sensor) {
        return new SensorDto(
                sensor.getSensorId(),
                sensor.getSensorName(),
                sensor.getSensorType(),
                sensor.getSensorStatus(),
                sensor.getDateAdded(),
                sensor.getBuilding() != null ? sensor.getBuilding().getBuildingId() : null
        );
    }
}
