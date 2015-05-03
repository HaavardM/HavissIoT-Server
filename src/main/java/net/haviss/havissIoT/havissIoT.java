package net.haviss.havissIoT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
    private static String brokerAddress;
    private static String clientID;
    private static String cmdTopic;
    private static String statusTopic;
    private static String databaseAddress;
    private static String database;
    private static int brokerPort;
    private static int qos;
    private static int databasePort;

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


}
