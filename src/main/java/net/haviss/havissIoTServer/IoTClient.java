package net.haviss.havissIoTServer;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;

/**
 * Created by Håvard Skåra Mellbye on 3/27/2015.
 * This library connects to an MQTT broker (using Paho MQTT libraries) and handles communication between the broker and client.
 *
 */
public class IoTClient {
    //Variables
    private String brokerAddress = "tcp://";
    private int brokerPort = 1883; //Default port is used if not else specified in connect
    private String clientID = "";
    private int qos = 2;
    //Objects
    private MqttClient mclient;
    private MemoryPersistence persistence = new MemoryPersistence();
    private MqttMessage recievedMessage; //Storing the last recieved message from subscribed topic
    private MqttCallback callback = new MqttCallback() {
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
    public IoTClient(String cID) {
        clientID = cID;
    }
        //Connect to broker
    public void connect(String address) {
        brokerAddress += (address + ":" + Integer.toString(brokerPort));
        try {
            mclient = new MqttClient(brokerAddress, clientID, persistence); //Setting up MQTT client and connect to broker
            mclient.setCallback(callback);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mclient.connect(connOpts);
        } catch (MqttException me) {
            //TODO: Handle exception
            me.printStackTrace();
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
            me.printStackTrace();
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
            me.printStackTrace();
        }
    }
    //Subscripe to topic
    public void subscribeToTopic(String topic, int qos) { //Subscribe to an MQTT topic
        try {
            mclient.subscribe(topic, qos);
        } catch (MqttException me) {
            //TODO: Handle exceptions
            me.printStackTrace();
        }
    }
    //Unsubscribe to topic
    public void unsubscribeToTopic(String topic) {
        try {
            mclient.unsubscribe(topic);
        } catch (MqttException me) {
            //TODO: Handle exceptions
            me.printStackTrace();
        }
    }
    //Get clientID
    public String getClientID() {
        return clientID;
    }

}
