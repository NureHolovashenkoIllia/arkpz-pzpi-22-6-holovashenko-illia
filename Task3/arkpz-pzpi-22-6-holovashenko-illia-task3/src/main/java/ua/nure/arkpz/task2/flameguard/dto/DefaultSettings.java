package ua.nure.arkpz.task2.flameguard.dto;

import java.math.BigDecimal;

public class DefaultSettings {

    private final float criticalValue;
    private final int measurementFrequency;
    private final BigDecimal serviceCost;

    public DefaultSettings(float criticalValue, int measurementFrequency, BigDecimal serviceCost) {
        this.criticalValue = criticalValue;
        this.measurementFrequency = measurementFrequency;
        this.serviceCost = serviceCost;
    }

    public float getCriticalValue() {
        return criticalValue;
    }

    public int getMeasurementFrequency() {
        return measurementFrequency;
    }

    public BigDecimal getServiceCost() {
        return serviceCost;
    }

    // Helper method to fetch default settings for a given sensor type
    public static DefaultSettings getDefaultSettings(String sensorType) {
        switch (sensorType) {
            case "Temperature":
                return new DefaultSettings(65.0f, 5, BigDecimal.valueOf(100.00));
            case "Gas":
                return new DefaultSettings(30.0f, 5, BigDecimal.valueOf(150.00));
            case "Smoke":
                return new DefaultSettings(50.0f, 5, BigDecimal.valueOf(120.00));
            case "Humidity":
                return new DefaultSettings(20.0f, 5, BigDecimal.valueOf(90.00));
            default:
                throw new IllegalArgumentException("Unsupported sensor type: " + sensorType);
        }
    }
}