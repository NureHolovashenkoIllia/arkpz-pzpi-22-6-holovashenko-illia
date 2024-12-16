package ua.nure.arkpz.task2.flameguard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.arkpz.task2.flameguard.entity.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
}
