package ua.nure.arkpz.task2.flameguard.dto;

import java.time.LocalDate;

public class BuildingDto {

    private Integer buildingId;

    private String buildingName;

    private String buildingDescription;

    private String buildingType;

    private LocalDate creationDate;

    private Integer userAccountId;

    public BuildingDto(Integer buildingId, String buildingName, String buildingDescription,
                       String buildingType, LocalDate creationDate, Integer userAccountId) {
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.buildingDescription = buildingDescription;
        this.buildingType = buildingType;
        this.creationDate = creationDate;
        this.userAccountId = userAccountId;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingDescription() {
        return buildingDescription;
    }

    public void setBuildingDescription(String buildingDescription) {
        this.buildingDescription = buildingDescription;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }
}
