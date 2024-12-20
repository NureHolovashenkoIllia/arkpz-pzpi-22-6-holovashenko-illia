package ua.nure.arkpz.task2.flameguard.dto;

import java.time.LocalDateTime;

public class MeasurementDto {

    private Integer measurementId;

    private Float measurementValue;

    private String measurementUnit;

    private LocalDateTime dateTimeReceived;

    private Integer sensorId;

    public MeasurementDto(Integer measurementId, Float measurementValue, String measurementUnit, LocalDateTime dateTimeReceived, Integer sensorId) {
        this.measurementId = measurementId;
        this.measurementValue = measurementValue;
        this.measurementUnit = measurementUnit;
        this.dateTimeReceived = dateTimeReceived;
        this.sensorId = sensorId;
    }

    public Integer getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(Integer measurementId) {
        this.measurementId = measurementId;
    }

    public Float getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(Float measurementValue) {
        this.measurementValue = measurementValue;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public LocalDateTime getDateTimeReceived() {
        return dateTimeReceived;
    }

    public void setDateTimeReceived(LocalDateTime dateTimeReceived) {
        this.dateTimeReceived = dateTimeReceived;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }
}
