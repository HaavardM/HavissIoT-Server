package net.haviss.havissIoTClientJava;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;

/**
 * Created by Håvard on 3/27/2015.
 * Main class - for now just testing the code - publishes a random number each 5. second
 */
public class Main {
    public static void main(String[] args) {
        String brokerAddress = "havissiot.cloudapp.net"; //Broker address
        int brokerPort = 1883; //Broker port
        String clientID = "havissRPI"; //Client id
        int qos = 2;
        havissIoTClient client = new havissIoTClient(clientID); //Creates a new client object
        client.connect(brokerAddress, brokerPort); //Connects to broker
        //Runs forever (or until program is terminated
        while(true) {
            Random rn = new Random(); //New random
            int number = rn.nextInt((1023 - 0) + 1) + 0; //New random number
            client.publishMessage("havissIOT/soverom/RPI/rand", Integer.toString(number)); //Publish a message to topic with random number
            try {
                Thread.sleep(5000); //Sleep for 5 seconds before publishing a new message
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}