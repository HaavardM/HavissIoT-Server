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
    public static List<String> topics = new ArrayList<>();
    private static String brokerAddress = "tcp://";
    private static int brokerPort = 1883; //Default port is used if not else specified in connect
    private static String clientID = "";
    private static int qos = 2;

    //Objects
    private static MqttClient mclient;
    private static MemoryPersistence persistence = new MemoryPersistence();

    //Connect to broker
    public static void connect(String address, String cID) {
        connect(address, brokerPort, cID);
    }

    //Overloaded function for connect - use with other ports than default
    public static void connect(String address, int port, String cID) {
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
    public static void setCallback(MqttCallback tempCallBack) {
        mclient.setCallback(tempCallBack);
    }

    //Disconnect from the server
    public static void disconnect() {
        try {
            mclient.disconnect();
        } catch (MqttException me) {
            //TODO: Handle exception
            me.printStackTrace();
        }
    }

    //Publish a message to a given topic
    public static void publishMessage(String topic, String msg) {
        try {
            MqttMessage pubMessage = new MqttMessage(msg.getBytes());
            mclient.publish(topic, pubMessage);
        } catch (MqttException me) {
            //TODO: Handle exception
        }
    }

    //Subscripe to topic
    public static void subscribeToTopic(String topic, int qos) { //Subscribe to an MQTT topic
        try {
            mclient.subscribe(topic, qos);
        } catch (MqttException me) {
            //TODO: Handle exceptions
        }
    }

    //Unsubscribe to topic
    public static void unsubscribeToTopic(String topic) {
        try {
            mclient.unsubscribe(topic);
        } catch (MqttException me) {
            //TODO: Handle exceptions
        }
    }

    //Get clientID
    public static String getClientID() {
        return clientID;
    }

    //Get qos
    public static int getQOS() {
        return qos;
    }
}
