package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.dto.AddressDto;
import ua.nure.arkpz.task2.flameguard.entity.Address;
import ua.nure.arkpz.task2.flameguard.entity.Building;
import ua.nure.arkpz.task2.flameguard.repository.AddressRepository;
import ua.nure.arkpz.task2.flameguard.repository.BuildingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    // Retrieve all addresses
    public List<AddressDto> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(this::convertToAddressDto)
                .toList();
    }

    // Retrieve address for a specific building
    public Optional<AddressDto> getAddressByBuildingId(int buildingId) {
        return addressRepository.findAddressByBuilding_BuildingId(buildingId)
                .map(this::convertToAddressDto);
    }

    // Create a new address
    public Optional<AddressDto> createAddress(AddressDto addressDto) {
        Optional<Building> building = addressDto.getBuildingId() != null
                ? buildingRepository.findById(addressDto.getBuildingId())
                : Optional.empty();

        Address address = new Address();
        address.setCountry(addressDto.getCountry());
        address.setCity(addressDto.getCity());
        address.setStreet(addressDto.getStreet());
        address.setHouseNumber(addressDto.getHouseNumber());
        address.setApartmentNumber(addressDto.getApartmentNumber());
        address.setLongitude(addressDto.getLongitude());
        address.setLatitude(addressDto.getLatitude());
        building.ifPresent(address::setBuilding);

        Address savedAddress = addressRepository.save(address);
        return Optional.of(convertToAddressDto(savedAddress));
    }

    // Update an existing address
    public Optional<AddressDto> updateAddress(Integer id, AddressDto updatedAddressDto) {
        return addressRepository.findById(id)
                .map(address -> {
                    address.setCountry(updatedAddressDto.getCountry());
                    address.setCity(updatedAddressDto.getCity());
                    address.setRegion(updatedAddressDto.getRegion());
                    address.setStreet(updatedAddressDto.getStreet());
                    address.setHouseNumber(updatedAddressDto.getHouseNumber());
                    address.setApartmentNumber(updatedAddressDto.getApartmentNumber());
                    address.setLongitude(updatedAddressDto.getLongitude());
                    address.setLatitude(updatedAddressDto.getLatitude());

                    if (updatedAddressDto.getBuildingId() != null) {
                        Optional<Building> building = buildingRepository
                                .findById(updatedAddressDto.getBuildingId());
                        building.ifPresent(address::setBuilding);
                    } else {
                        address.setBuilding(null);
                    }

                    Address savedAddress = addressRepository.save(address);
                    return convertToAddressDto(savedAddress);
        });
    }

    // Update building_id field for a specific address
    public Optional<AddressDto> updateAddressBuilding(int id, Integer buildingId) {
        return addressRepository.findById(id)
                .map(address -> {

                    if (buildingId == null) {
                        throw new IllegalArgumentException("The address must contain a building");
                    } else {
                        if (buildingRepository.findById(buildingId).isEmpty()) {
                            throw new IllegalArgumentException("The specified building id " +
                                    buildingId + " does not exist");
                        } else {
                            Building newBuilding = buildingRepository.getReferenceById(buildingId);
                            address.setBuilding(newBuilding);
                        }
                    }

                    Address savedAddress = addressRepository.save(address);
                    return convertToAddressDto(savedAddress);
                });
    }

    // Delete an address
    public boolean deleteAddress(int id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AddressDto convertToAddressDto(Address address) {
        return new AddressDto(
                address.getAddressId(),
                address.getCountry(),
                address.getCity(),
                address.getRegion(),
                address.getStreet(),
                address.getHouseNumber(),
                address.getApartmentNumber(),
                address.getLongitude(),
                address.getLatitude(),
                address.getBuilding() != null ? address.getBuilding().getBuildingId() : null
        );
    }
}
