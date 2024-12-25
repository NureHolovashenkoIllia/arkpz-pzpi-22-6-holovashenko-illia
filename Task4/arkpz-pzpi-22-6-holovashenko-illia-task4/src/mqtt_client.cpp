#include "mqtt_client.h"
#include "sensors.h"
#include "utils.h"
#include <nlohmann/json.hpp>
#include <iostream>
#include <thread>
using json = nlohmann::json;

void publishSensorData(mqtt::async_client& client, const Config& config) {
    while (true) {
        std::vector<json> sensors = fetchSensors(config.sensor_api_url);
        std::cout << "Fetched sensors: " << sensors.size() << std::endl;

        std::unordered_map<int, std::unordered_map<std::string, double>> sensorValues;

        for (const auto& sensor : sensors) {
            std::string sensorType = sensor["sensorType"];
            SensorData value = generateSensorValue(sensorType, config);

            json payload = {
                {"measurementValue", value.value},
                {"measurementUnit", value.unit},
                {"dateTimeReceived", getCurrentTimeISO8601()},
                {"sensorId", sensor["sensorId"]}
            };

            std::cout << "Publishing data: " << payload.dump(4) << std::endl;
            client.publish(config.mqtt_topic, payload.dump(), config.mqtt_qos, false)->wait();
        }

        std::this_thread::sleep_for(std::chrono::seconds(config.publish_interval));
    }
}