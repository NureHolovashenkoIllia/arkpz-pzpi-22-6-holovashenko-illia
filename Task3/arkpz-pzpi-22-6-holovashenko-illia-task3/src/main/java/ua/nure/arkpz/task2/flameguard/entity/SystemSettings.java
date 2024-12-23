package ua.nure.arkpz.task2.flameguard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "System_settings")
public class SystemSettings {

    @Id
    @Size(max = 150)
    @Column(name = "Setting_Key", nullable = false, length = 150)
    private String settingKey;

    @Size(max = 255)
    @NotNull
    @Column(name = "Setting_Value", nullable = false, length = 255)
    private String settingValue;

    // Getters and Setters
    public @Size(max = 150) String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(@Size(max = 150) String settingKey) {
        this.settingKey = settingKey;
    }

    public @Size(max = 255) @NotNull String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(@Size(max = 255) @NotNull String settingValue) {
        this.settingValue = settingValue;
    }
}
