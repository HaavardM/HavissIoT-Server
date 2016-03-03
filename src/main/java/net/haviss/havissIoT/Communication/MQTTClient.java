package net.haviss.havissIoT.Communication;

import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.HavissIoT;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by haavardM on 3/27/2015.
 * This library connects to an MQTT broker (using Paho MQTT libraries) and handles communication between the broker and client.
 */
public class MQTTClient {

    //Variables
    private CopyOnWriteArrayList<String> topics = new CopyOnWriteArrayList<>();
    private String brokerAddress = "tcp://";
    private int brokerPort = 1883; //Default port is used if not else specified in connect
    private String clientID = "";
    private int qos = 2;
    private boolean connected = false;

    //Objects
    private MqttClient mclient;
    private MemoryPersistence persistence = new MemoryPersistence();

    public MQTTClient(String clientID) {
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
            connected = true;
        } catch (MqttException me) {
            HavissIoT.printMessage("MQTT connection error: " + me.getMessage() );
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
            connected = false;
        } catch (MqttException me) {
            //TODO: Handle exception
            HavissIoT.printMessage(me.getMessage());
        }
    }

    //Publish a message to a given topic
    public synchronized void publishMessage(String topic, String msg) throws HavissIoTMQTTException {
        try {
            MqttMessage pubMessage = new MqttMessage(msg.getBytes());
            mclient.publish(topic, pubMessage);
        } catch (MqttException me) {
            //TODO: Handle exception
            throw new HavissIoTMQTTException(me.getMessage());
        }
    }

    //Subscribe to topic
    public synchronized void subscribeToTopic(String topic, int qos) throws HavissIoTMQTTException{ //Subscribe to an MQTT topic
        try {
            if(!topics.contains(topic)) {
                mclient.subscribe(topic, qos);
                topics.add(topic);
            }
        } catch (MqttException me) {
            //TODO: Handle exceptions
            throw new HavissIoTMQTTException(me.getMessage());
        }
    }

    //Unsubscribe to topic
    public synchronized void unsubscribeToTopic(String topic) throws HavissIoTMQTTException {
        try {
            mclient.unsubscribe(topic);
            topics.remove(topic);
        } catch (MqttException me) {
            throw new HavissIoTMQTTException(me.getMessage());
        }
    }

    //Get all available topics
    public synchronized String[] getTopics() {
        return topics.toArray(new String[topics.size()]);
    }

    //Get clientID
    public synchronized String getClientID() {
        return clientID;
    }

    //Get qos
    public synchronized int getQOS() {
        return qos;
    }

    //Check if connected
    public synchronized boolean isConnected() {
        return this.connected;
    }



}
