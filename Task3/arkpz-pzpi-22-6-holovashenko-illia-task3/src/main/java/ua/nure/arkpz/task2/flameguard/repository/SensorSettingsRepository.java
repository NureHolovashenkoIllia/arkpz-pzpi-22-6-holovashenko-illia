package ua.nure.arkpz.task2.flameguard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.arkpz.task2.flameguard.entity.Sensor;
import ua.nure.arkpz.task2.flameguard.entity.SensorSettings;

import java.util.Optional;

@Repository
public interface SensorSettingsRepository extends JpaRepository<SensorSettings, Integer> {
    Optional<SensorSettings> findBySensor(Sensor sensor);
}
