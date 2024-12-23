package ua.nure.arkpz.task2.flameguard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.entity.*;
import ua.nure.arkpz.task2.flameguard.repository.AddressRepository;
import ua.nure.arkpz.task2.flameguard.repository.AlarmRepository;
import ua.nure.arkpz.task2.flameguard.repository.SensorRepository;
import ua.nure.arkpz.task2.flameguard.util.EmailHelper;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private EmailHelper emailHelper;

    public void sendAlarmNotification(Integer alarmId, Measurement measurement) {
        try {
            Alarm alarm = getAlarmById(alarmId);
            UserAccount user = getUserFromBuilding(alarm.getSensor().getBuilding());
            String buildingInfo = getBuildingAddress(alarm.getSensor().getBuilding());

            String alarmDetails = String.format(
                    "Alarm Notification\n\n" +
                            "Alarm Details:\n" +
                            "Type: %s\n" +
                            "Occurred At: %s\n" +
                            "Sensor Type: %s\n" +
                            "Sensor Measurement: %s\n\n" +
                            "Location:\n%s",
                    alarm.getAlarmType(),
                    alarm.getTimeOccurred().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss")),
                    alarm.getSensor().getSensorType(),
                    measurement.getMeasurementValue() + " " + measurement.getMeasurementUnit(),
                    buildingInfo
            );

            emailHelper.sendEmail(user.getEmail(), "Alarm Notification", alarmDetails);
        } catch (Exception e) {
            logger.error("Error sending alarm notification", e);
        }
    }

    public void sendAlarmNotification(Integer alarmId) {
        try {
            Alarm alarm = getAlarmById(alarmId);
            UserAccount user = getUserFromBuilding(alarm.getSensor().getBuilding());
            String buildingInfo = getBuildingAddress(alarm.getSensor().getBuilding());

            String alarmDetails = String.format(
                    "Alarm Notification\n\n" +
                            "Alarm Details:\n" +
                            "Type: %s\n" +
                            "Occurred At: %s\n" +
                            "Location:\n%s",
                    alarm.getAlarmType(),
                    alarm.getTimeOccurred().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss")),
                    buildingInfo
            );

            emailHelper.sendEmail(user.getEmail(), "Alarm Notification", alarmDetails);
        } catch (Exception e) {
            logger.error("Error sending alarm notification", e);
        }
    }

    public void sendSensorStatusNotification(Integer sensorId) {
        try {
            Sensor sensor = getSensorById(sensorId);
            String address = getAddressForSensor(sensor);
            UserAccount userAccount = getUserAccountForSensor(sensor);
            String userFullName = userAccount.getFirstName() + " " + userAccount.getLastName();

            String subject = "Faulty Sensor Notification";
            String body = String.format(
                    "Dear %s,\n\n" +
                            "The sensor '%s' located at '%s' has been marked as 'Faulty'.\n" +
                            "Please take the necessary actions to inspect or replace the sensor.\n\n" +
                            "Thank you,\nFlameGuard Team",
                    userFullName,
                    sensor.getSensorName(),
                    address
            );

            emailHelper.sendEmail(userAccount.getEmail(), subject, body);
        } catch (Exception e) {
            logger.error("Error sending sensor status notification", e);
        }
    }

    private Alarm getAlarmById(Integer alarmId) throws Exception {
        return alarmRepository.findById(alarmId)
                .orElseThrow(() -> new Exception("Alarm not found"));
    }

    private UserAccount getUserFromBuilding(Building building) throws Exception {
        return Optional.ofNullable(building.getUserAccount())
                .orElseThrow(() -> new Exception("User not found"));
    }

    private String getBuildingAddress(Building building) {
        return addressRepository.findAddressByBuilding_BuildingId(building.getBuildingId())
                .map(address -> String.format(
                        "Building: %s\nAddress: %s, %s, %s, %s, %s, %s\n",
                        building.getBuildingName(),
                        address.getCountry(),
                        address.getCity(),
                        address.getRegion(),
                        address.getStreet(),
                        address.getHouseNumber(),
                        address.getApartmentNumber() != null ? address.getApartmentNumber() : "N/A"
                ))
                .orElse("Address information is not available.");
    }

    private Sensor getSensorById(Integer sensorId) throws Exception {
        return sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found with ID: " + sensorId));
    }

    private String getAddressForSensor(Sensor sensor) {
        return addressRepository.findAddressByBuilding_BuildingId(sensor.getBuilding().getBuildingId())
                .map(address -> String.format(
                        "%s, %s, %s, %s, %s, %s",
                        address.getCountry(),
                        address.getCity(),
                        address.getRegion(),
                        address.getStreet(),
                        address.getHouseNumber(),
                        address.getApartmentNumber() != null ? address.getApartmentNumber() : "N/A"
                ))
                .orElse("Address information is not available.");
    }

    private UserAccount getUserAccountForSensor(Sensor sensor) throws RuntimeException {
        UserAccount userAccount = sensor.getBuilding().getUserAccount();
        if (userAccount == null || userAccount.getEmail() == null) {
            throw new RuntimeException("User email not found for sensor ID");
        }
        return userAccount;
    }
}
