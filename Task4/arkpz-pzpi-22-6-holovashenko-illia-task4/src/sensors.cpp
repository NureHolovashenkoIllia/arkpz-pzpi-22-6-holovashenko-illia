#include "sensors.h"
#include "utils.h"
#include <unordered_map>

std::unordered_map<std::string, double> generateSensorValues() {
    return {
            {"Temperature", generateRandomValue(20.0, 50.0)},
            {"Humidity", generateRandomValue(10.0, 90.0)},
            {"Gas", generateRandomValue(0.0, 100.0)},
            {"Smoke", generateRandomValue(0.0, 10.0)}
    };
}
