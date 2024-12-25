#ifndef CONFIG_H
#define CONFIG_H

#include <string>

struct SensorLimits {
    int min;
    int max;
};

struct Config {
    std::string mqtt_address;
    std::string mqtt_client_id;
    std::string mqtt_topic;
    int mqtt_qos;
    std::string sensor_api_url;
    SensorLimits temperature_limits;
    SensorLimits humidity_limits;
    SensorLimits gas_limits;
    SensorLimits smoke_limits;
    int publish_interval;
};

Config loadConfig(const std::string& filename);

#endif // CONFIG_H