package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.entity.SensorSettings;
import ua.nure.arkpz.task2.flameguard.repository.SensorSettingsRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class SensorSettingsService {

    @Autowired
    private SensorSettingsRepository sensorSettingsRepository;

    public Optional<SensorSettings> updateCriticalValue(Integer id, Float criticalValue) {
        return updateField(id, settings -> settings.setSensorCriticalValue(criticalValue));
    }

    public Optional<SensorSettings> updateMeasurementFrequency(Integer id, Integer measurementFrequency) {
        return updateField(id, settings -> settings.setMeasurementFrequency(measurementFrequency));
    }

    public Optional<SensorSettings> updateServiceCost(Integer id, BigDecimal serviceCost) {
        return updateField(id, settings -> settings.setServiceCost(serviceCost));
    }

    private Optional<SensorSettings> updateField(Integer id, FieldUpdater updater) {
        Optional<SensorSettings> settingsOptional = sensorSettingsRepository.findById(id);
        if (settingsOptional.isPresent()) {
            SensorSettings settings = settingsOptional.get();
            updater.update(settings);
            sensorSettingsRepository.save(settings);
            return Optional.of(settings);
        }
        return Optional.empty();
    }

    @FunctionalInterface
    private interface FieldUpdater {
        void update(SensorSettings settings);
    }
}