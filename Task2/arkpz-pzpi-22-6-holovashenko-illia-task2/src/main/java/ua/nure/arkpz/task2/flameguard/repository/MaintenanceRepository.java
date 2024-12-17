package ua.nure.arkpz.task2.flameguard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.arkpz.task2.flameguard.entity.Maintenance;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    List<Maintenance> findMaintenanceByBuilding_BuildingId(Integer buildingId);
}
