package ua.nure.arkpz.task2.flameguard.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;
import ua.nure.arkpz.task2.flameguard.service.MeasurementService;

@Component
public class MqttListener {

    private static final String SERVER_URI = "tcp://localhost:1883";
    private static final String CLIENT_ID = "FireSafetySubscriberClient";
    private static final String TOPIC = "fire_safety/data";

    private final MeasurementService measurementService;
    private final ObjectMapper objectMapper;

    public MqttListener(MeasurementService measurementService, ObjectMapper objectMapper) {
        this.measurementService = measurementService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void connectAndSubscribe() {
        try {
            MqttClient client = new MqttClient(SERVER_URI, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(30);
            options.setKeepAliveInterval(60);

            System.out.println("Connecting to MQTT broker...");
            client.connect(options);
            System.out.println("Connected to MQTT broker.");

            client.subscribe(TOPIC, (topic, message) -> {
                String payload = new String(message.getPayload());
                System.out.println("Message received: " + payload);
            });

            System.out.println("Subscribe to the topic: " + TOPIC);

        } catch (MqttException e) {
            System.err.println("Error connecting to MQTT broker: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error connecting to MQTT broker: " + e.getMessage(), e);
        }
    }
}
