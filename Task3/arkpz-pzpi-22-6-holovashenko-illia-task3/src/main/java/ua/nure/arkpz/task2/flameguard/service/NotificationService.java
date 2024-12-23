package ua.nure.arkpz.task2.flameguard.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.entity.*;
import ua.nure.arkpz.task2.flameguard.repository.AddressRepository;
import ua.nure.arkpz.task2.flameguard.repository.AlarmRepository;
import ua.nure.arkpz.task2.flameguard.repository.UserAccountRepository;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendAlarmNotification(Integer alarmId, Measurement measurement) throws Exception {
        // Retrieve alarm from repository
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new Exception("Alarm not found"));

        Building building = alarm.getSensor().getBuilding();

        // Retrieve associated user account
        UserAccount user = Optional.ofNullable(building.getUserAccount())
                .orElseThrow(() -> new Exception("User not found"));

        // Get detailed alarm information
        String sensorType = alarm.getSensor().getSensorType();
        String sensorMeasurement = measurement.getMeasurementValue() + " " + measurement.getMeasurementUnit();

        // Retrieve building address
        String buildingInfo = addressRepository.findAddressByBuilding_BuildingId(building.getBuildingId())
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

        // Format alarm details
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
                sensorType,
                sensorMeasurement,
                buildingInfo
        );

        sendEmail(user.getEmail(), "Alarm Notification", alarmDetails);
    }

    public void sendAlarmNotification(Integer alarmId) throws Exception {
        // Retrieve alarm from repository
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new Exception("Alarm not found"));

        Building building = alarm.getSensor().getBuilding();

        // Retrieve associated user account
        UserAccount user = Optional.ofNullable(building.getUserAccount())
                .orElseThrow(() -> new Exception("User not found"));

        // Retrieve building
        String buildingInfo = addressRepository.findAddressByBuilding_BuildingId(building.getBuildingId())
                .map(address -> String.format(
                        "Building: %s\n",
                        building.getBuildingName()
                ))
                .orElse("Address information is not available.");

        // Format alarm details
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

        sendEmail(user.getEmail(), "Alarm Notification", alarmDetails);
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);
        mailSender.send(message);
    }
}
