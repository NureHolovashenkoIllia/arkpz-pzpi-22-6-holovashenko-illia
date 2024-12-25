#include <string>
#include <cpprest/http_client.h>
#include <nlohmann/json.hpp>
using json = nlohmann::json;

std::string analyseBuildingCondition(const std::unordered_map<std::string, double>& sensorValues,
                                     const std::string& buildingType,
                                     const std::unordered_map<std::string, std::unordered_map<std::string, double>>& weights) {

    if (weights.find(buildingType) == weights.end()) {
        throw std::invalid_argument("Unknown building type: " + buildingType);
    }

    const auto& buildingWeights = weights.at(buildingType);
    double score = 0.0;
    double totalWeight = 0.0;

    // Normalize sensor values and calculate weighted score
    for (const auto& [sensorType, value] : sensorValues) {
        if (buildingWeights.find(sensorType) != buildingWeights.end()) {
            double normalizedValue = 1.0 - std::exp(-value / 100.0);
            score += normalizedValue * buildingWeights.at(sensorType);
            totalWeight += buildingWeights.at(sensorType);
        }
    }

    // Calculate final score as a percentage
    score = (totalWeight > 0) ? (score / totalWeight) * 100 : 0;

    if (score >= 90) return "Excellent";
    if (score >= 80) return "Good";
    if (score >= 60) return "Fair";
    if (score >= 40) return "Poor";
    return "Dangerous";
}

json fetchBuildingById(const std::string& apiUrl, int buildingId) {
    std::string buildingUrl = apiUrl + "/" + std::to_string(buildingId);

    web::http::client::http_client client(utility::conversions::to_string_t(buildingUrl));

    web::http::http_response response = client.request(web::http::methods::GET).get();

    if (response.status_code() != web::http::status_codes::OK) {
        throw std::runtime_error("Failed to fetch building details for buildingId " + std::to_string(buildingId) +
                                 ", HTTP Status: " + std::to_string(response.status_code()));
    }

    auto responseBody = response.extract_string().get();
    return json::parse(responseBody);
}