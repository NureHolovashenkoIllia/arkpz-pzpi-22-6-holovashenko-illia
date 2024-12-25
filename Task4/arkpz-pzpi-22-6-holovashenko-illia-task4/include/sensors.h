#ifndef SENSORS_H
#define SENSORS_H

#include <config.h>
#include <unordered_map>
#include <string>
#include <nlohmann/json.hpp>
using json = nlohmann::json;

struct SensorData {
  	double value;
    std::string unit;
};

SensorData generateSensorValue(const std::string& sensor_type, const Config& config);

std::vector<json> fetchSensors(const std::string& apiUrl);

#endif // SENSORS_H