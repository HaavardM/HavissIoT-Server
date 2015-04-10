package net.haviss.havissIoTServer;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Håvard on 3/27/2015.
 */
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
        int databasePort = 27017;
        //Objects
        HavissIoTClient client;
        Properties prop = new Properties();
        HavissIoTStorage storage;
        try {
            prop.load(new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/config.properties"))));
            brokerAddress = prop.getProperty("brokerAddress");
            brokerPort = Integer.parseInt(prop.getProperty("brokerPort"));
            qos = Integer.parseInt(prop.getProperty("mqttqos"));
            clientID = prop.getProperty("clientID");
            databaseAddress = prop.getProperty("databaseAddress");
            databasePort = Integer.parseInt(prop.getProperty("databasePort"));
        } catch (IOException e) {
            //TODO: Handle exception
            e.printStackTrace();
        }

        client = new HavissIoTClient(clientID);

        //Print settings
        System.out.println("MQTT broker settings:");
        System.out.println("Broker address:\t " + brokerAddress);
        System.out.println("Broker port:\t" + Integer.toString(brokerPort));
        System.out.println("Client id:\t" + clientID);
        System.out.println("QOS:\t" + Integer.toString(qos));
        System.out.println("\nDatabase settings:");
        System.out.println("Database address:\t" + databaseAddress);
        System.out.println("Database port:\t" + Integer.toString(databasePort));
        //Connecting to broker
        System.out.println("\nConnecting to broker");
        client.connect(brokerAddress, brokerPort);
        System.out.println("Connected to " + brokerAddress);

        //Connecting to database
        System.out.println("\nConnecting to database");
        storage = new HavissIoTStorage(databaseAddress, databasePort);
        System.out.println("Connected to " + databaseAddress);
        //Starting storage thread
        storage.start();
        MqttCallback callback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                storage.addValues(s, mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        };

    }

}
