package net.haviss.havissIoTClientJava;


import java.io.*;
import java.util.Properties;
import java.util.Random;

/**
 * Created by Håvard on 3/27/2015.
 * Main class - for now just testing the code - publishes a random number each 5. second
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Properties config = new Properties();
        try {
            config.load(new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/config.properties"))));
        } catch (Exception e) {
            //TODO:Handle exception
            e.printStackTrace();
        }
        //Get properties from config file
        String brokerAddress = config.getProperty("brokerAddress");
        String clientID = config.getProperty("clientID");
        String topic = config.getProperty("topic");
        int brokerPort = 1883; //Default value
        int qos = 2; //Default value
        int delayInterval = 5000; //Default value
        try {
            brokerPort = Integer.parseInt(config.getProperty("brokerPort"));
            qos = Integer.parseInt(config.getProperty("mqttqos"));
            delayInterval = Integer.parseInt(config.getProperty("delayInterval"));
        } catch(NumberFormatException e) {
            //TODO: Handle exception
            e.printStackTrace();
        }

        System.out.println("Broker address: " + brokerAddress + ":" + Integer.toString(brokerPort));
        System.out.println("Topic: " + topic);
        System.out.println("Client ID: " + clientID);

        //Connect to broker
        havissIoTClient client = new havissIoTClient(clientID);
        client.connect(brokerAddress, brokerPort);
        //Random number - since I dont have a sensor connected
        Random ran = new Random();
        //Run forever!
        while(true) {
            //TODO: Publish something
            client.publishMessage(topic, Integer.toString(ran.nextInt(1024)));
            Thread.sleep(delayInterval);
            System.out.println("Published!");
        }










    }

}