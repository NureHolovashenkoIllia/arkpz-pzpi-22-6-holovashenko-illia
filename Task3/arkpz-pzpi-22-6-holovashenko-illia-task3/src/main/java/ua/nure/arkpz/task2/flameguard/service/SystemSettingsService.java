package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.entity.SystemSettings;
import ua.nure.arkpz.task2.flameguard.repository.SystemSettingsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SystemSettingsService {

    @Autowired
    private SystemSettingsRepository systemSettingsRepository;

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
}