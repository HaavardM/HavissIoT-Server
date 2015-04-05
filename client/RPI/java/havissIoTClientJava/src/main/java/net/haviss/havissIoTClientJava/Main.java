package net.haviss.havissIoTClientJava;


/**
 * Created by Håvard on 3/27/2015.
 * Main class - for now just testing the code - publishes a random number each 5. second
 */
public class Main {
    public static void main(String[] args) {
        configHandler config = new configHandler("config.properties");
        //Get properties from config file
        String brokerAddress = config.loadPropValue("brokerAddress");
        int brokerPort = Integer.parseInt(config.loadPropValue("brokerPort"));
        String clientID = config.loadPropValue("clientID");
        int qos = Integer.parseInt(config.loadPropValue("mqttqos"));
        int delayInterval = Integer.parseInt(config.loadPropValue("intervall"));
        String topic = config.loadPropValue("topic");
        //Connect to broker
        havissIoTClient client = new havissIoTClient(clientID);
        client.connect(brokerAddress);
        while(true) {
            //TODO: Publish something
        }









    }

}