#include "sensors.h"
#include "utils.h"
#include <config.h>
#include <nlohmann/json.hpp>
#include <cmath>
#include <cpprest/http_client.h>
using json = nlohmann::json;

SensorData generateSensorValue(const std::string& sensor_type, const Config& config) {
    double value;
    std::string unit;

    if (sensor_type == "Temperature") {
        value = generateRandomValue(config.temperature_limits.min, config.temperature_limits.max);
        unit = "°C";
    } else if (sensor_type == "Humidity") {
        value = generateRandomValue(config.humidity_limits.min, config.humidity_limits.max);
        unit = "%";
    } else if (sensor_type == "Gas") {
        value = generateRandomValue(config.gas_limits.min, config.gas_limits.max);
        unit = "ppm";
    } else if (sensor_type == "Smoke") {
        value = generateRandomValue(config.smoke_limits.min, config.smoke_limits.max);
        unit = "%";
    } else {
        throw std::invalid_argument("Unsupported sensor type: " + sensor_type);
    }

    value = std::round(value * 10000) / 10000; // Round to 4 decimal places
    return {value, unit};
}

std::vector<json> fetchSensors(const std::string& apiUrl) {
    web::http::client::http_client client(utility::conversions::to_string_t(apiUrl));

    web::http::http_response response = client.request(web::http::methods::GET).get();

    if (response.status_code() != web::http::status_codes::OK) {
        throw std::runtime_error("Failed to fetch sensors, HTTP Status: " + std::to_string(response.status_code()));
    }

    auto responseBody = response.extract_string().get();
    return json::parse(responseBody);
}
