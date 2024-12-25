#ifndef CONFIG_H
#define CONFIG_H

#include <string>

struct Config {
    std::string mqtt_address;
    std::string mqtt_client_id;
    std::string mqtt_topic;
    int mqtt_qos;
    int publish_interval;
};

Config loadConfig(const std::string& filename);

#endif // CONFIG_H