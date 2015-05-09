package net.haviss.havissIoT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by HaavardM on 5/3/2015.
 * Main class
 */
public class HavissIoT {

    /*Objects*/
    public static IoTClient client;
    public static IoTStorage storage;
    public static final Object threadLock = new Object();
    private static CopyOnWriteArrayList<String> toPrint;

    //Main method
    public static void main(String args[]) {

        //Load logger and config
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.WARNING);
        Config.loadConfig("/config.properties");

        //Load config from file
        System.out.println("Settings:\n");
        printSettings();

        //Initialize toPrint list.
        toPrint = new CopyOnWriteArrayList<>();

        //Initialize IoT client
        client = new IoTClient(Config.clientID);
        client.connect(Config.brokerAddress, Config.brokerPort);


        //Setting up new callback for client
        MqttCallback callback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                //TODO: Handle connection lost
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                if (s.compareTo(Config.cmdTopic) == 0) {
                    new CommandHandler().processCommand(mqttMessage.toString());
                } else if(storage.getTopicsToStore().contains(s)) {
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
        storage = new IoTStorage(Config.databaseAddress, Config.databasePort, Config.database);
        storage.start();

        //Objects for command handling
        SocketCommunication socketCommunication = new SocketCommunication(Config.serverPort, Config.numbOfClients);

        //Waits for storage thread to be done with console
        while(storage.getThreadConsole());

        //Everything is started
        printMessage("Application is ready");

        //Application must run forever
        while(true) {
            //If there is something to print
            if(toPrint.size() > 0) {
                for(int i = 0; i < toPrint.size(); i++) {
                    System.out.println(new Date().toString() + " " + toPrint.get(i)); //Printing to console with date
                    toPrint.remove(i); //Remove from list
                }
            } else {
                try {
                    synchronized (threadLock) {
                        threadLock.wait(); //no need to steal cycles
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void printSettings() {
        System.out.println("MQTT broker settings:\n");
        System.out.println("Broker address:\t " + Config.brokerAddress);
        System.out.println("Broker port:\t" + Integer.toString(Config.brokerPort));
        System.out.println("Client id:\t" + Config.clientID);
        System.out.println("QOS:\t" + Integer.toString(Config.qos));
        System.out.println("\nDatabase settings:\n");
        System.out.println("Database address:\t" + Config.databaseAddress);
        System.out.println("Database port:\t" + Integer.toString(Config.databasePort));
        System.out.println("Database:\t" + Config.database);
        System.out.println("\nServer settings:\n");
        System.out.println("Server port:\t" + Integer.toString(Config.serverPort));
        System.out.println("Number of clients:\t" + Integer.toString(Config.numbOfClients));
        System.out.print("\n");
    }

    //Add new message and notify thread
    public static synchronized void printMessage(String message) {
        toPrint.add(message);
        synchronized (threadLock) {
            threadLock.notify();
        }
    }



}
