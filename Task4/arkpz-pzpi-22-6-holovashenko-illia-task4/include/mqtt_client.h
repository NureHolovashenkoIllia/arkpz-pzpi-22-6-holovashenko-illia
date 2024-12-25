#ifndef MQTT_CLIENT_H
#define MQTT_CLIENT_H

#include <mqtt/async_client.h>
#include "config.h"

void publishSensorData(mqtt::async_client& client, const Config& config);

#endif // MQTT_CLIENT_H