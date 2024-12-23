package ua.nure.arkpz.task2.flameguard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.arkpz.task2.flameguard.entity.Sensor;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    List<Sensor> findByBuilding_BuildingId(Integer buildingId);
    List<Sensor> findAllBySensorStatus(String status);
}
