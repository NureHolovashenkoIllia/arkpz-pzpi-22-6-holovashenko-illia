package ua.nure.arkpz.task2.flameguard.dto;

import java.math.BigDecimal;

public class AddressDto {

    private Integer addressId;

    private String country;

    private String city;

    private String region;

    private String street;

    private String houseNumber;

    private String apartmentNumber;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private Integer buildingId;

    public AddressDto(Integer addressId, String country, String city, String region,
                      String street, String houseNumber, String apartmentNumber,
                      BigDecimal longitude, BigDecimal latitude, Integer buildingId) {
        this.addressId = addressId;
        this.country = country;
        this.city = city;
        this.region = region;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.buildingId = buildingId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }
}
