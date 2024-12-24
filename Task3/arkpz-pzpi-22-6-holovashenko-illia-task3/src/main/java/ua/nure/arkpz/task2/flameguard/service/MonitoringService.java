package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.dto.DefaultSettings;
import ua.nure.arkpz.task2.flameguard.entity.Sensor;
import ua.nure.arkpz.task2.flameguard.entity.SensorSettings;
import ua.nure.arkpz.task2.flameguard.repository.SensorRepository;
import ua.nure.arkpz.task2.flameguard.repository.SensorSettingsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MonitoringService {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SensorSettingsRepository sensorSettingsRepository;

    @Scheduled(fixedDelayString = "#{@systemSettingsService.getMeasurementsCheckInterval()}") // Виконувати кожні 10 хвилин
    public void checkSensorStatus() {
        List<Sensor> sensors = sensorRepository.findAllBySensorStatus("Enabled");

        for (Sensor sensor : sensors) {
            if (isSensorFaulty(sensor)) {
                sensorService.updateSensorStatus(sensor.getSensorId(), "Faulty");

                // Генерувати сповіщення для користувача
                notificationService.sendSensorStatusNotification(sensor.getSensorId());
            }
        }
    }

    private boolean isSensorFaulty(Sensor sensor) {
        Optional<SensorSettings> sensorSettingsOptional = sensorSettingsRepository.findBySensor(sensor);

        if (sensorSettingsOptional.isEmpty()) {
            throw new RuntimeException("Sensor settings not found for sensor ID: " + sensor.getSensorId());
        }

        Integer allowedInactivityMinutes = sensorSettingsOptional.get().getMeasurementFrequency();

        LocalDateTime lastData = sensor.getLastDataReceived();
        if (lastData == null) {
            return true;
        }
        return lastData.isBefore(LocalDateTime.now().minusMinutes(allowedInactivityMinutes));
    }
}
