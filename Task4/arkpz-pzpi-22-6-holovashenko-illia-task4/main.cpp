#include <iostream>
#include "config.h"
#include "mqtt_client.h"

int main() {
    try {
        Config config = loadConfig("config.json");

        mqtt::async_client client(config.mqtt_address, config.mqtt_client_id);

        mqtt::connect_options connOpts;
        std::cout << "Connecting to MQTT broker..." << std::endl;
        client.connect(connOpts)->wait();
        std::cout << "Connected to MQTT broker." << std::endl;

        publishSensorData(client, config);

        std::cout << "Disconnecting from MQTT broker..." << std::endl;
        client.disconnect()->wait();
        std::cout << "Disconnected." << std::endl;

    } catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << std::endl;
    }

    return 0;
}
