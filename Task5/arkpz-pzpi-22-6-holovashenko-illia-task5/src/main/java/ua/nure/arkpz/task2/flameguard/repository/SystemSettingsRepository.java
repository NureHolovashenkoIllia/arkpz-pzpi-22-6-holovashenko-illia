package ua.nure.arkpz.task2.flameguard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.arkpz.task2.flameguard.entity.SystemSettings;

import java.util.Optional;

@Repository
public interface SystemSettingsRepository extends JpaRepository<SystemSettings, String> {
    Optional<SystemSettings> findBySettingKey(String settingKey);
}