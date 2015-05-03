package net.haviss.havissIoT;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Håvard Skåra Mellbye on 3/27/2015.
 * This library connects to an MQTT broker (using Paho MQTT libraries) and handles communication between the broker and client.
 */
public class IoTClient {

    //Variables
    public List<String> topics = new ArrayList<>();
    private String brokerAddress = "tcp://";
    private int brokerPort = 1883; //Default port is used if not else specified in connect
    private String clientID = "";
    private int qos = 2;

    //Objects
    private static MqttClient mclient;
    private static MemoryPersistence persistence = new MemoryPersistence();

    public IoTClient(String clientID) {
        this.clientID = clientID;
    }

    //Connect to broker
    public void connect(String address) {
        connect(address, brokerPort);
    }

    //Overloaded function for connect - use with other ports than default
    public void connect(String address, int port) {
        brokerPort = port;
        brokerAddress += (address + ":" + Integer.toString(brokerPort));
        try {
            mclient = new MqttClient(brokerAddress, clientID, persistence); //Setting up MQTT client and connect to broker
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mclient.connect(connOpts); //Connecting to broker
        } catch (MqttException me) {
            //TODO: Handle exception
        }
    }

    //Set new callback for MQTT-Client
    public void setCallback(MqttCallback tempCallBack) {
        mclient.setCallback(tempCallBack);
    }

    //Disconnect from the server
    public void disconnect() {
        try {
            mclient.disconnect();
        } catch (MqttException me) {
            //TODO: Handle exception
            me.printStackTrace();
        }
    }

    //Publish a message to a given topic
    public void publishMessage(String topic, String msg) {
        try {
            MqttMessage pubMessage = new MqttMessage(msg.getBytes());
            mclient.publish(topic, pubMessage);
        } catch (MqttException me) {
            //TODO: Handle exception
        }
    }

    //Subscripe to topic
    public void subscribeToTopic(String topic, int qos) { //Subscribe to an MQTT topic
        try {
            mclient.subscribe(topic, qos);
        } catch (MqttException me) {
            //TODO: Handle exceptions
        }
    }

    //Unsubscribe to topic
    public void unsubscribeToTopic(String topic) {
        try {
            mclient.unsubscribe(topic);
        } catch (MqttException me) {
            //TODO: Handle exceptions
        }
    }

    //Get clientID
    public String getClientID() {
        return clientID;
    }

    //Get qos
    public int getQOS() {
        return qos;
    }
}
