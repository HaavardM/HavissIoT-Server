package net.haviss.havissIoTClientJava;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;

/**
 * Created by Håvard Skåra Mellbye on 3/27/2015.
 * This library connects to an MQTT broker (using Paho MQTT libraries) and handles communication between the broker and client.
 *
 */
public class havissIoTClient {
    //Variables
    private static String brokerAddress = "tcp://";
    private static int brokerPort = 1883; //Default port is used if not else specified in connect
    private static String clientID = "";
    private static int qos = 2;
    //Objects
    private static MqttClient mclient;
    private configHandler config = new configHandler("config.properties");
    private static MemoryPersistence persistence = new MemoryPersistence();
    private static MqttMessage recievedMessage; //Storing the last recieved message from subscribed topic
    private static MqttCallback callback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable throwable) {
            //TODO: Handle loss of connection to broker
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            //TODO: Handle response to message arriving on topic
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            //TODO: Handle response to delivery of published message completed
        }
    };
    //Class constructor
    public havissIoTClient(String cID) {
        clientID = cID;
    }
        //Connect to broker
    public static void connect(String address) {
        brokerAddress += (address + ":" + Integer.toString(brokerPort));
        try {
            mclient = new MqttClient(brokerAddress, clientID, persistence); //Setting up MQTT client and connect to broker
            mclient.setCallback(callback);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mclient.connect(connOpts);
        } catch (MqttException me) {
            //TODO: Handle exception
        }
    }
    //Overloaded function for connect - use with other ports than default
    public void connect(String address, int port) {
        brokerPort = port;
        brokerAddress += (address + ":" + Integer.toString(brokerPort));
        try {
            mclient = new MqttClient(brokerAddress, clientID, persistence); //Setting up MQTT client and connect to broker
            mclient.setCallback(callback);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mclient.connect(connOpts); //Connecting to broker
        } catch (MqttException me) {
            //TODO: Handle exception
        }
    }
    //Set new callback for MQTT-Client
    public void setCallback(MqttCallback tempCallBack) {
            callback = tempCallBack;
            mclient.setCallback(tempCallBack);
    }
    //Disconnect from the server
    public void disconnect() {
        try {
            mclient.disconnect();
        } catch (MqttException me) {
            //TODO: Handle exception
        }
    }
    //Publish a message to a given topic
    public void publishMessage(String topic, String msg) {
        try {
            MqttMessage pubMessage = new MqttMessage(msg.getBytes(StandardCharsets.UTF_8));
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

}
