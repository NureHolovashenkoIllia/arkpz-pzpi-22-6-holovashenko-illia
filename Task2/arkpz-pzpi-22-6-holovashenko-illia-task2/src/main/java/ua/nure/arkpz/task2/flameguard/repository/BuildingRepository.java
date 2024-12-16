package ua.nure.arkpz.task2.flameguard.repository;

import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.arkpz.task2.flameguard.entity.Building;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Integer> {
    List<Building> findByUserAccount_UserAccountId(Integer userAccountId);
}
