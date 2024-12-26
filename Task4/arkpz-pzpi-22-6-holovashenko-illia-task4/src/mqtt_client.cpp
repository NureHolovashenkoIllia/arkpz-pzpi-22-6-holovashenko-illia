#include "mqtt_client.h"
#include "..//include//sensors.h"
#include "buildings.h"
#include "utils.h"
#include <nlohmann/json.hpp>
#include <iostream>
#include <thread>
#include <unordered_map>
#include <chrono>
using json = nlohmann::json;

namespace {

    void processSensor(const json& sensor, mqtt::async_client& client, const Config& config, std::unordered_map<int, std::unordered_map<std::string, double>>& sensorValues) {
        try {
            std::string sensorType = sensor.at("sensorType");
            SensorData value = generateSensorValue(sensorType, config);

            json measurementPayload = {
                {"measurementValue", value.value},
                {"measurementUnit", value.unit},
                {"dateTimeReceived", getCurrentTimeISO8601()},
                {"sensorId", sensor.at("sensorId")}
            };

            std::cout << "Publishing data: " << measurementPayload.dump(4) << std::endl;
            client.publish(config.mqtt_topic, measurementPayload.dump(), config.mqtt_qos, false)->wait();

            int buildingId = sensor.at("buildingId");
            sensorValues[buildingId][sensorType] = value.value;
        } catch (const std::exception& e) {
            std::cerr << "Error processing sensor: " << e.what() << std::endl;
        }
    }

    void processBuilding(int buildingId, const std::unordered_map<std::string, double>& sensorValuesForBuilding, mqtt::async_client& client, const Config& config) {
        try {
            auto buildingDetails = fetchBuildingById(config.building_api_url, buildingId);
            std::cout << "Fetched building details: " << buildingDetails.dump(4) << std::endl;

            std::string buildingType = buildingDetails.at("buildingType");
            std::string updatedCondition = analyseBuildingCondition(sensorValuesForBuilding, buildingType, config.sensor_weights);

            std::cout << "Building condition: " << updatedCondition << std::endl;

            json statusPayload = {
                {"buildingId", buildingId},
                {"buildingCondition", updatedCondition}
            };

            std::cout << "Publishing status: " << statusPayload.dump(4) << std::endl;
            client.publish(config.mqtt_topic, statusPayload.dump(), config.mqtt_qos, false)->wait();
        } catch (const std::exception& e) {
            std::cerr << "Error processing building data: " << e.what() << std::endl;
        }
    }

    void fetchAndProcessSensors(mqtt::async_client& client, const Config& config, std::unordered_map<int, std::unordered_map<std::string, double>>& sensorValues) {
        try {
            auto sensors = fetchSensors(config.sensor_api_url);
            std::cout << "Fetched sensors: " << sensors.size() << std::endl;

            for (const auto& sensor : sensors) {
                processSensor(sensor, client, config, sensorValues);
            }
        } catch (const std::exception& e) {
            std::cerr << "Error fetching sensor data: " << e.what() << std::endl;
        }
    }

    void processBuildings(mqtt::async_client& client, const Config& config, const std::unordered_map<int, std::unordered_map<std::string, double>>& sensorValues) {
        for (const auto& [buildingId, sensorValuesForBuilding] : sensorValues) {
            processBuilding(buildingId, sensorValuesForBuilding, client, config);
        }
    }

}

void publishSensorData(mqtt::async_client& client, const Config& config) {
    while (true) {
        std::unordered_map<int, std::unordered_map<std::string, double>> sensorValues;

        fetchAndProcessSensors(client, config, sensorValues);
        processBuildings(client, config, sensorValues);

        std::cout << std::endl << std::endl;
        std::this_thread::sleep_for(std::chrono::seconds(config.publish_interval));
    }
}
