package ua.nure.arkpz.task2.flameguard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.entity.BackupData;
import ua.nure.arkpz.task2.flameguard.entity.SystemSettings;
import ua.nure.arkpz.task2.flameguard.repository.SystemSettingsRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Service
public class SystemSettingsService {

    @Autowired
    private SystemSettingsRepository systemSettingsRepository;

    private final DataSource dataSource;

    private static final Logger logger = LoggerFactory.getLogger(SystemSettingsService.class);

    public SystemSettingsService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<SystemSettings> getAllSettings() {
        return systemSettingsRepository.findAll();
    }

    public Optional<SystemSettings> updateSettingValue(String key, String value) {
        Optional<SystemSettings> settingOptional = systemSettingsRepository.findById(key);
        if (settingOptional.isPresent()) {
            SystemSettings setting = settingOptional.get();
            setting.setSettingValue(value);
            systemSettingsRepository.save(setting);
            return Optional.of(setting);
        }
        return Optional.empty();
    }

    public void createDatabaseBackup(String backupFilePath) {
        String backupQuery = String.format("BACKUP DATABASE FlameGuard TO DISK = '%s'", backupFilePath);

        executeDatabaseOperation(backupQuery, "Backup created successfully", "Error creating database backup");
    }

    public void restoreDatabase(String backupFilePath) {
        String switchToMasterQuery = "USE master;";
        String setSingleUserQuery = "ALTER DATABASE FlameGuard SET SINGLE_USER WITH ROLLBACK IMMEDIATE;";
        String restoreQuery = String.format("RESTORE DATABASE FlameGuard FROM DISK = '%s' WITH REPLACE", backupFilePath);
        String setMultiUserQuery = "ALTER DATABASE FlameGuard SET MULTI_USER;";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(switchToMasterQuery);
            logger.info("Switched to master database.");

            executeStatement(statement, setSingleUserQuery, "Database set to SINGLE_USER mode.");
            executeStatement(statement, restoreQuery, "Database restored successfully.");
            executeStatement(statement, setMultiUserQuery, "Database set to MULTI_USER mode.");

        } catch (SQLException e) {
            logger.error("Error restoring database: {}", e.getMessage(), e);
            throw new RuntimeException("Error restoring database", e);
        }
    }

    private void executeDatabaseOperation(String query, String successMessage, String errorMessage) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(query);
            logger.info(successMessage);

        } catch (SQLException e) {
            logger.error("{}: {}", errorMessage, e.getMessage(), e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private void executeStatement(Statement statement, String query, String successMessage) throws SQLException {
        statement.execute(query);
        logger.info(successMessage);
    }

    public SystemSettings saveOrUpdateSetting(String settingKey, String settingValue) {
        Optional<SystemSettings> existingSetting = systemSettingsRepository.findById(settingKey);

        SystemSettings systemSettings = existingSetting.orElse(new SystemSettings());
        systemSettings.setSettingKey(settingKey);
        systemSettings.setSettingValue(settingValue);

        return systemSettingsRepository.save(systemSettings);
    }

    public String getMeasurementsCheckInterval() {
        SystemSettings setting = systemSettingsRepository.findById("Measurements_Check_Interval")
                .orElseThrow(() -> new RuntimeException("Measurements Check Interval setting not found in SystemSettings"));
        return setting.getSettingValue();
    }
}