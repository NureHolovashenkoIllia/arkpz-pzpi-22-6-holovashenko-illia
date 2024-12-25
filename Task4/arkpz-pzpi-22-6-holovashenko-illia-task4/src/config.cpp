#include "config.h"
#include <fstream>
#include <nlohmann/json.hpp>

using json = nlohmann::json;

Config loadConfig(const std::string& filename) {
    std::ifstream configFile(filename);
    if (!configFile) {
        throw std::runtime_error("Failed to open config file");
    }

    json configJson;
    configFile >> configJson;

    Config config;
    config.mqtt_address = configJson["mqtt_broker"]["address"];
    config.mqtt_client_id = configJson["mqtt_broker"]["client_id"];
    config.mqtt_topic = configJson["mqtt_broker"]["topic"];
    config.mqtt_qos = configJson["mqtt_broker"]["qos"];
    config.sensor_api_url = configJson["sensor_api"]["url"];
    config.building_api_url = configJson["building_api"]["url"];
    config.publish_interval = configJson["publish_interval"];

    // Load sensor limits
    config.temperature_limits.min = configJson["sensor_limits"]["Temperature"]["min"];
    config.temperature_limits.max = configJson["sensor_limits"]["Temperature"]["max"];
    config.humidity_limits.min = configJson["sensor_limits"]["Humidity"]["min"];
    config.humidity_limits.max = configJson["sensor_limits"]["Humidity"]["max"];
    config.gas_limits.min = configJson["sensor_limits"]["Gas"]["min"];
    config.gas_limits.max = configJson["sensor_limits"]["Gas"]["max"];
    config.smoke_limits.min = configJson["sensor_limits"]["Smoke"]["min"];
    config.smoke_limits.max = configJson["sensor_limits"]["Smoke"]["max"];

    // Load sensor weights
    for (const auto& [buildingType, weights] : configJson["sensor_weights"].items()) {
        for (const auto& [sensorType, weight] : weights.items()) {
            config.sensor_weights[buildingType][sensorType] = weight.get<double>();
        }
    }

    return config;
}
