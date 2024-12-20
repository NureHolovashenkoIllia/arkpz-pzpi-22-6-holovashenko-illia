package ua.nure.arkpz.task2.flameguard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Address_id", nullable = false)
    private Integer addressId;

    @Size(max = 50)
    @Column(name = "Country", length = 50)
    private String country;

    @Size(max = 50)
    @Column(name = "City", length = 50)
    private String city;

    @Size(max = 50)
    @Column(name = "Region", length = 50)
    private String region;

    @Size(max = 100)
    @Column(name = "Street", length = 100)
    private String street;

    @Column(name = "House_number", length = 10)
    private String houseNumber;

    @Size(max = 10)
    @Column(name = "Apartment_number", length = 10)
    private String apartmentNumber;

    @NotNull
    @Column(name = "Longitude", precision = 9, scale = 6, nullable = false)
    private BigDecimal longitude;

    @NotNull
    @Column(name = "Latitude", precision = 9, scale = 6, nullable = false)
    private BigDecimal latitude;

    @OneToOne
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Building_id", unique = true, nullable = false)
    private Building building;

    // Getters and Setters
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

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
