package ua.nure.arkpz.task2.flameguard.dto;

import java.time.LocalDateTime;

public class AlarmDto {

    private Integer alarmId;

    private String alarmType;

    private LocalDateTime timeOccurred;

    private Boolean isResolved;

    private LocalDateTime timeResolved;

    private Integer sensorId;

    public AlarmDto(Integer alarmId, String alarmType, LocalDateTime timeOccurred,
                    Boolean isResolved, LocalDateTime timeResolved, Integer sensorId) {
        this.alarmId = alarmId;
        this.alarmType = alarmType;
        this.timeOccurred = timeOccurred;
        this.isResolved = isResolved;
        this.timeResolved = timeResolved;
        this.sensorId = sensorId;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public LocalDateTime getTimeOccurred() {
        return timeOccurred;
    }

    public void setTimeOccurred(LocalDateTime timeOccurred) {
        this.timeOccurred = timeOccurred;
    }

    public Boolean getResolved() {
        return isResolved;
    }

    public void setResolved(Boolean resolved) {
        isResolved = resolved;
    }

    public LocalDateTime getTimeResolved() {
        return timeResolved;
    }

    public void setTimeResolved(LocalDateTime timeResolved) {
        this.timeResolved = timeResolved;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }
}
