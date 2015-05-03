package net.haviss.havissIoT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Håvard on 5/3/2015.
 */
public class havissIoT {
    /*Objects*/
    private static Logger mongoLogger;
    public static Config config;
    public static IoTClient client;
    public static IoTStorage storage;
    private static MqttCallback callback;

    /*Variables*/
    public static String brokerAddress;
    public static String clientID;
    public static String cmdTopic;
    public static String statusTopic;
    public static String databaseAddress;
    public static String database;
    public static int brokerPort;
    public static int qos;
    public static int databasePort;

    static {
        //Load logger and config
        mongoLogger = Logger.getLogger("or.mongodb.org");
        mongoLogger.setLevel(Level.WARNING);
        config = new Config("/config.properties");

        //Load config from file
        brokerAddress = config.getProperty("broker_address");
        clientID = config.getProperty("client_id");
        cmdTopic = config.getProperty("cmd_topic");
        statusTopic = config.getProperty("status_topic");
        databaseAddress = config.getProperty("database_address");
        database = config.getProperty("database");
        brokerPort = Integer.parseInt(config.getProperty("broker_port"));
        qos = Integer.parseInt(config.getProperty("mqtt_qos"));
        databasePort = Integer.parseInt(config.getProperty("database_port"));

        //Initialize IoT client
        client = new IoTClient(clientID);
        client.connect(brokerAddress, brokerPort);
        callback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                //TODO: Handle connection lost
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                if(s.compareTo(cmdTopic) == 0) {
                    //TODO: Handle commands
                } else {
                    havissIoT.storage.addValues(s, mqttMessage.toString());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                //No messages is delivered - should never be called
            }
        };
        client.setCallback(callback);

        //Initialize storage client
        storage = new IoTStorage(databaseAddress, databasePort, database);
        storage.start();
    }
    public void printSettings() {
        System.out.println("MQTT broker settings:");
        System.out.println("Broker address:\t " + brokerAddress);
        System.out.println("Broker port:\t" + Integer.toString(brokerPort));
        System.out.println("Client id:\t" + clientID);
        System.out.println("QOS:\t" + Integer.toString(qos));
        System.out.println("\nDatabase settings:");
        System.out.println("Database address:\t" + databaseAddress);
        System.out.println("Database port:\t" + Integer.toString(databasePort));
        System.out.println("Database:\t" + database);
    }


}
