#ifndef BUILDINGS_H
#define BUILDINGS_H

#include <string>
#include <nlohmann/json.hpp>
using json = nlohmann::json;

std::string analyseBuildingCondition(const std::unordered_map<std::string, double>& sensorValues,
                                     const std::string& buildingType,
                                     const std::unordered_map<std::string, std::unordered_map<std::string, double>>& weights);

json fetchBuildingById(const std::string& apiUrl, int buildingId);

#endif //BUILDINGS_H
