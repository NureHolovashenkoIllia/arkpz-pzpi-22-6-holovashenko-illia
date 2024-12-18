package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.dto.AlarmDto;
import ua.nure.arkpz.task2.flameguard.entity.Alarm;
import ua.nure.arkpz.task2.flameguard.entity.Sensor;
import ua.nure.arkpz.task2.flameguard.repository.AlarmRepository;
import ua.nure.arkpz.task2.flameguard.repository.SensorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlarmService {

    @Autowired
    private AlarmRepository alarmRepository;
    @Autowired
    private SensorRepository sensorRepository;

    // Retrieve all alarms
    public List<AlarmDto> getAllAlarms() {
        return alarmRepository.findAll().stream()
                .map(this::convertToAlarmDto)
                .toList();
    }

    // Retrieve an alarm by ID
    public Optional<AlarmDto> getAlarmById(int id) {
        return alarmRepository.findById(id)
                .map(this::convertToAlarmDto);
    }

    // Retrieve all alarms for a specific sensor
    public List<AlarmDto> getAlarmsBySensorId(int sensorId) {
        return alarmRepository.findAlarmBySensor_SensorId(sensorId)
                .stream()
                .map(this::convertToAlarmDto)
                .toList();
    }

    // Retrieve all alarms for a specific building
    public List<AlarmDto> getAlarmsByBuildingId(int buildingId) {
        return alarmRepository.findAlarmBySensor_Building_BuildingId(buildingId)
                .stream()
                .map(this::convertToAlarmDto)
                .toList();
    }

    // Filter alarms by type
    public List<AlarmDto> getAlarmsByType(String type) {
        return alarmRepository.findAll()
                .stream()
                .filter(alarm -> alarm.getAlarmType().equalsIgnoreCase(type))
                .map(this::convertToAlarmDto)
                .toList();
    }

    // Create a new alarm
    public Optional<AlarmDto> createAlarm(AlarmDto alarmDto) {
        validateAlarmDto(alarmDto);

        Optional<Sensor> sensor = alarmDto.getSensorId() != null
                ? sensorRepository.findById(alarmDto.getSensorId())
                : Optional.empty();

        Alarm alarm = new Alarm();
        alarm.setAlarmId(alarmDto.getAlarmId());
        alarm.setAlarmType(alarmDto.getAlarmType());
        alarm.setTimeOccurred(alarmDto.getTimeOccurred());
        alarm.setIsResolved(alarmDto.getResolved());
        alarm.setTimeResolved(alarmDto.getTimeResolved());
        sensor.ifPresent(alarm::setSensor);

        Alarm savedAlarm = alarmRepository.save(alarm);
        return Optional.of(convertToAlarmDto(savedAlarm));
    }

    // Update an existing alarm
    public Optional<AlarmDto> updateAlarm(int id, AlarmDto updatedAlarmDto) {
        return alarmRepository.findById(id)
                .map(alarm -> {
                    alarm.setAlarmType(updatedAlarmDto.getAlarmType());
                    alarm.setTimeOccurred(updatedAlarmDto.getTimeOccurred());
                    alarm.setIsResolved(updatedAlarmDto.getResolved());
                    alarm.setTimeResolved(updatedAlarmDto.getTimeResolved());

                    if (updatedAlarmDto.getSensorId() != null) {
                        Optional<Sensor> sensor = sensorRepository
                                .findById(updatedAlarmDto.getSensorId());
                        sensor.ifPresent(alarm::setSensor);
                    } else {
                        alarm.setSensor(null);
                    }

                    validateAlarmDto(convertToAlarmDto(alarm));

                    Alarm savedAlarm = alarmRepository.save(alarm);
                    return convertToAlarmDto(savedAlarm);
        });
    }

    // Delete an alarm
    public boolean deleteAlarm(Integer id) {
        if (alarmRepository.existsById(id)) {
            alarmRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Validate alarm fields
    private void validateAlarmDto(AlarmDto alarmDto) {
        if (!alarmDto.getAlarmType().matches("Fire alarm|Gas Leak|Power Outage|System Failure")) {
            throw new IllegalArgumentException("Invalid alarm type: " + alarmDto.getAlarmType());
        }
    }

    private AlarmDto convertToAlarmDto(Alarm alarm) {
        return new AlarmDto(
                alarm.getAlarmId(),
                alarm.getAlarmType(),
                alarm.getTimeOccurred(),
                alarm.getIsResolved(),
                alarm.getTimeResolved(),
                alarm.getSensor() != null ? alarm.getSensor().getSensorId() : null
        );
    }
}
