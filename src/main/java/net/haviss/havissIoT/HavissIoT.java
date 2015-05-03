package net.haviss.havissIoT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Håvard on 5/3/2015.
 * Using this class for easier access to different objects from other classes.
 * Also makes Main cleaner
 */
public class HavissIoT {
    /*Objects*/
    public static Config config;
    public static IoTClient client;
    public static IoTStorage storage;

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

    public static void start() {
        //Load logger and config
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
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
        System.out.println("Settings:\n");
        printSettings();
        //Initialize IoT client
        client = new IoTClient(clientID);
        client.connect(brokerAddress, brokerPort);

        //Setting up new callback for client
        MqttCallback callback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                //TODO: Handle connection lost
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                if (s.compareTo(cmdTopic) == 0) {
                    //TODO: Handle commands
                } else {
                    HavissIoT.storage.addValues(s, mqttMessage.toString());
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
    public static void printSettings() {
        System.out.println("MQTT broker settings:\n");
        System.out.println("Broker address:\t " + brokerAddress);
        System.out.println("Broker port:\t" + Integer.toString(brokerPort));
        System.out.println("Client id:\t" + clientID);
        System.out.println("QOS:\t" + Integer.toString(qos));
        System.out.println("\nDatabase settings:\n");
        System.out.println("Database address:\t" + databaseAddress);
        System.out.println("Database port:\t" + Integer.toString(databasePort));
        System.out.println("Database:\t" + database);
    }


}
