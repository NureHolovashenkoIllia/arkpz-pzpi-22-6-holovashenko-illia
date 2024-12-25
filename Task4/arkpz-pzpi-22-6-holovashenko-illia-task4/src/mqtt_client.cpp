#include "mqtt_client.h"
#include "sensors.h"
#include "utils.h"
#include <nlohmann/json.hpp>
#include <iostream>
#include <thread>
using json = nlohmann::json;

void publishSensorData(mqtt::async_client& client, const Config& config) {
    while (true) {
        auto sensorValues = generateSensorValues();
        json payload = {
            {"timestamp", getCurrentTimeISO8601()},
            {"sensors", sensorValues}
        };

        std::cout << "Publishing: " << payload.dump(4) << std::endl;
        client.publish(config.mqtt_topic, payload.dump(), config.mqtt_qos, false)->wait();

        std::this_thread::sleep_for(std::chrono::seconds(config.publish_interval));
    }
}