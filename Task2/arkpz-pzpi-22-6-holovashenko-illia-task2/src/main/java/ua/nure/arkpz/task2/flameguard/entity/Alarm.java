package ua.nure.arkpz.task2.flameguard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "Alarm")
public class Alarm {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Alarm_id", nullable = false)
    private Integer alarmId;

    @NotNull
    @Size(max = 50)
    @Column(name = "Alarm_type", length = 50, nullable = false)
    private String alarmType;

    @NotNull
    @Column(name = "Time_occurred", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timeOccurred = LocalDateTime.now();

    @NotNull
    @Column(name = "Is_resolved", nullable = false)
    private Boolean isResolved;

    @Column(name = "Time_resolved")
    private LocalDateTime timeResolved;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Sensor_id")
    private Sensor sensor;

    @OnDelete(action = OnDeleteAction.SET_NULL)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Building_id")
    private Building building;

    // Getters and Setters
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

    public Boolean getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    public LocalDateTime getTimeResolved() {
        return timeResolved;
    }

    public void setTimeResolved(LocalDateTime timeResolved) {
        this.timeResolved = timeResolved;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @PrePersist
    public void validateAlarmFields() {
        if (!alarmType.matches("Fire alarm|Gas Leak|Power Outage|System Failure")) {
            throw new IllegalArgumentException("Invalid alarm type: " + alarmType);
        }
    }
}