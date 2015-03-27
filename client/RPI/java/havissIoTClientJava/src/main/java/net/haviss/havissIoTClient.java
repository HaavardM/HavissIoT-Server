package net.haviss;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Håvard on 3/27/2015.
 */
public class havissIoTClient extends messageGenerator {
    //Variables
    private String brokerAddress = "tcp://";
    private int brokerPort = 1883; //Default port is used if not else specified in connect
    private String clientID;
    private int qos = 2;
    //Objects
    private MqttClient mclient;
    private MemoryPersistence persistence = new MemoryPersistence();
    private MqttMessage recievedMessage; //Storing the recieved message
    private MqttCallback callback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable throwable) {
            //TODO: Throw exception and reconnect
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            recievedMessage = mqttMessage;
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            //TODO: Add resposne to complete delivery
        }
    }; //Callback for MQTTclient


    //Class constructor
    public havissIoTClient(String ID) {
        clientID = ID;
    }
    //Connect to broker
    public void connect(String address) {
        brokerAddress += (address + ":" + Integer.toString(brokerPort));
        try {
            mclient = new MqttClient(brokerAddress, clientID, persistence); //Setting up MQTT client and connect to broker
            mclient.setCallback(callback); //Set callbackfunctions
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
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mclient.connect(connOpts); //Connecting to broker
        } catch (MqttException me) {
            //TODO: Handle exception
        }
    }
    //Publish a message to a given topic
    public void publishMessage(String topic, String message) {
        try {
            MqttMessage pubMessage = new MqttMessage(message.getBytes());
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

}
