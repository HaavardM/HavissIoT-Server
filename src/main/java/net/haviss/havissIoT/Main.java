package net.haviss.havissIoT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String args[]) {
        //Topics
        List<String> topics = new ArrayList<>();

        //MQTT settings
        String brokerAddress = "";
        String clientID = "";
        int brokerPort = 1883;
        int qos = 2;

        //Database settings
        String databaseAddress = "";
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver"); //Do not disturb console with unnecessary information
        mongoLogger.setLevel(Level.WARNING);
        int databasePort = 27017;
        String database = "";

        //Objects
        IoTClient client;
        Properties prop = new Properties();
        final IoTStorage storage;
        Scanner scanner = new Scanner(System.in);
        Config config = new Config("/config.properties");
        client = new IoTClient(clientID);

        //Load properties strings from file
        try {
            brokerAddress = config.getProperty("broker_address");
            clientID = config.getProperty("client_id");
            databaseAddress = config.getProperty("database_address");
            database = config.getProperty("database");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Load properties values from file
        brokerPort = Integer.parseInt(config.getProperty("broker_port"));
        qos = Integer.parseInt(config.getProperty("mqtt_qos"));
        databasePort = Integer.parseInt(config.getProperty("database_port"));

        //Print settings
        System.out.println("MQTT broker settings:");
        System.out.println("Broker address:\t " + brokerAddress);
        System.out.println("Broker port:\t" + Integer.toString(brokerPort));
        System.out.println("Client id:\t" + clientID);
        System.out.println("QOS:\t" + Integer.toString(qos));
        System.out.println("\nDatabase settings:");
        System.out.println("Database address:\t" + databaseAddress);
        System.out.println("Database port:\t" + Integer.toString(databasePort));
        System.out.println("Database:\t" + database);

        //Connecting to broker
        System.out.println("\nConnecting to broker...");
        client.connect(brokerAddress, brokerPort);
        System.out.println("Connected to " + brokerAddress);

        //Connecting to database
        System.out.println("\nConnecting to database");
        storage = new IoTStorage(databaseAddress, databasePort, database);
        System.out.println("Connected to " + databaseAddress);
        System.out.println("Using database " + database + "\n");

        //Starting storage thread
        storage.start();

        //New callback methods for MQTT client
        MqttCallback callback = new MqttCallback() {

            @Override
            public void connectionLost(Throwable throwable) {
                //TODO: Handle connection lost
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                storage.addValues(s, mqttMessage.toString()); //Add values to storage handler

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                //No messages is delivered - should never be called
            }
        };

        //Set new callback functions
        client.setCallback(callback);

        //Check for new topics - subscribing to topics
        while (storage.getThreadConsole()) ;
        System.out.println("\n\n");

        while (true) {
            System.out.print("Enter command: ");
            String text = scanner.nextLine();
            text = text.toLowerCase();
            if (text.length() > 0) {
                String[] commands = text.split("\\s+");
                if(commands[0].compareTo("help") == 0) ConsoleCommands.printHelp();
                else if (commands[0].compareTo("exit") == 0) System.exit(0);
                else if(commands[0].compareTo("add") == 0) {
                    for(int i = 1; i < commands.length; i++) {
                        client.subscribeToTopic(commands[i], qos);
                        topics.add(commands[i]);
                    }
                    System.out.println("Subscribed to " + Integer.toString(commands.length - 1) + " topics");
                } else if(commands[0].compareTo("topics") == 0) {
                    System.out.println("Subscribed to these topics:");
                    for (String s : topics ) {
                        System.out.println(s);
                    }
                }
            }
        }
    }
}
