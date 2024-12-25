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
    config.publish_interval = configJson["publish_interval"];

    return config;
}
