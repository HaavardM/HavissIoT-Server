package net.haviss.havissIoT;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by H�vard on 3/27/2015.
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
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        int databasePort = 27017;
        String database = "";
        //Objects
        IoTClient client;
        Properties prop = new Properties();
        final IoTStorage storage;
        Scanner scanner = new Scanner(System.in);
        try {
            prop.load(new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/config.properties"))));
            brokerAddress = prop.getProperty("brokerAddress");
            brokerPort = Integer.parseInt(prop.getProperty("brokerPort"));
            qos = Integer.parseInt(prop.getProperty("mqttqos"));
            clientID = prop.getProperty("clientID");
            databaseAddress = prop.getProperty("databaseAddress");
            databasePort = Integer.parseInt(prop.getProperty("databasePort"));
            database = prop.getProperty("database");
        } catch (IOException e) {
            //TODO: Handle exception
            e.printStackTrace();
        }
        //New IoTClient with correct settings
        client = new IoTClient(clientID);
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
        System.out.println("\nConnecting to broker");
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
                synchronized (storage.lock) {
                    storage.lock.notify(); //Resumes thread after wait
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                //No messages is delivered - should never be called
            }
        };

        //Set new callback functions
        client.setCallback(callback);
        //Check for new topics - subscribing to topics
        while(storage.isThreadBusy());
        System.out.println("\n\n");
        while(true) {
            System.out.print("Enter new topic or \"exit\": ");
            String topic = scanner.nextLine();
            topic.toLowerCase();
            if(topic.length() > 0) {
                if(topic.compareTo("exit") == 0) {
                    System.exit(0);
                }
                client.subscribeToTopic(topic, qos);
                System.out.println("Subscribed!");
                topics.add(topic);
            }
        }

    }

}
