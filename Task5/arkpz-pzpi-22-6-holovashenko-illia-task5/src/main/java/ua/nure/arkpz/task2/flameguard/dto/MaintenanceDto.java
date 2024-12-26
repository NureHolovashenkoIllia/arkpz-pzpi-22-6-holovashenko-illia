package ua.nure.arkpz.task2.flameguard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MaintenanceDto {

    private Integer maintenanceId;

    private LocalDate datePerformed;

    private String workDescription;

    private BigDecimal cost;

    private Integer buildingId;

    public MaintenanceDto(Integer maintenanceId, LocalDate datePerformed, String workDescription, BigDecimal cost, Integer buildingId) {
        this.maintenanceId = maintenanceId;
        this.datePerformed = datePerformed;
        this.workDescription = workDescription;
        this.cost = cost;
        this.buildingId = buildingId;
    }

    public Integer getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(Integer maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public LocalDate getDatePerformed() {
        return datePerformed;
    }

    public void setDatePerformed(LocalDate datePerformed) {
        this.datePerformed = datePerformed;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }
}
