package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.dto.MeasurementDto;
import ua.nure.arkpz.task2.flameguard.dto.SensorDto;
import ua.nure.arkpz.task2.flameguard.entity.Measurement;
import ua.nure.arkpz.task2.flameguard.entity.Sensor;
import ua.nure.arkpz.task2.flameguard.repository.MeasurementRepository;
import ua.nure.arkpz.task2.flameguard.repository.SensorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeasurementService {

    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private SensorRepository sensorRepository;

    // Retrieve all measurements
    public List<MeasurementDto> getAllMeasurements() {
        return measurementRepository.findAll().stream()
                .map(this::convertToMeasurementDto)
                .toList();
    }

    // Retrieve a specific measurement by ID
    public Optional<MeasurementDto> getMeasurementById(int id) {
        return measurementRepository.findById(id)
                .map(this::convertToMeasurementDto);
    }

    // Retrieve measurements by sensor ID
    public List<MeasurementDto> getMeasurementsBySensorId(int sensorId) {
        return measurementRepository.findMeasurementsBySensor_SensorId(sensorId)
                .stream()
                .map(this::convertToMeasurementDto)
                .toList();
    }

    // Filter measurements by unit for a specific sensor
    public List<MeasurementDto> filterMeasurementsByUnit(int sensorId, String unit) {
        return measurementRepository.findAll()
                .stream()
                .filter(measurement -> measurement.getSensor().getSensorId() == sensorId
                        && unit.equals(measurement.getMeasurementUnit()))
                .map(this::convertToMeasurementDto)
                .toList();
    }

    // Create a new measurement
    public Optional<MeasurementDto> createMeasurement(MeasurementDto measurementDto) {
        Optional<Sensor> sensor = measurementDto.getSensorId() != null
                ? sensorRepository.findById(measurementDto.getSensorId())
                : Optional.empty();

        Measurement measurement = new Measurement();
        measurement.setMeasurementId(measurementDto.getMeasurementId());
        measurement.setMeasurementValue(measurementDto.getMeasurementValue());
        measurement.setMeasurementUnit(measurementDto.getMeasurementUnit());
        measurement.setDateTimeReceived(measurementDto.getDateTimeReceived());
        sensor.ifPresent(measurement::setSensor);

        Measurement savedMeasurement = measurementRepository.save(measurement);
        return Optional.of(convertToMeasurementDto(savedMeasurement));
    }

    // Delete a measurement
    public boolean deleteMeasurement(Integer id) {
        if (measurementRepository.existsById(id)) {
            measurementRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private MeasurementDto convertToMeasurementDto(Measurement measurement) {
        return new MeasurementDto(
                measurement.getMeasurementId(),
                measurement.getMeasurementValue(),
                measurement.getMeasurementUnit(),
                measurement.getDateTimeReceived(),
                measurement.getSensor() != null ? measurement.getSensor().getSensorId() : null
        );
    }
}

