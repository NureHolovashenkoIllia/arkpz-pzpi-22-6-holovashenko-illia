package ua.nure.arkpz.task2.flameguard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "Measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Measurement_id", nullable = false)
    private Integer measurementId;

    @NotNull
    @Column(name = "Measurement_value", nullable = false)
    private Float measurementValue;


    @NotNull
    @Size(max = 20)
    @Column(name = "Measurement_unit", length = 20, nullable = false)
    private String measurementUnit;

    @NotNull
    @Column(name = "Date_time_received", nullable = false)
    private LocalDateTime dateTimeReceived;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Sensor_id", nullable = false)
    private Sensor sensor;

    // Getters and Setters
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

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
