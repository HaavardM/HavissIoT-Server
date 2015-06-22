package net.haviss.havissIoT;

import com.mongodb.MongoException;
import net.haviss.havissIoT.Communication.IoTClient;
import net.haviss.havissIoT.Communication.SocketServer;
import net.haviss.havissIoT.Storage.IoTStorage;
import net.haviss.havissIoT.Core.SensorHandler;
import net.haviss.havissIoT.Core.UserHandler;
import net.haviss.havissIoT.External.PublicIP;
import net.haviss.havissIoT.Sensor.IoTSensor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    public static UserHandler userHandler;
    public static final SensorHandler sensorHandler = new SensorHandler();
    public static final Object threadLock = new Object();
    public static CopyOnWriteArrayList<Thread> allThreads;
    private static CopyOnWriteArrayList<String> toPrint;

    //Main method
    public static void main(String args[]) {

        //Load logger and config
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        Config.loadConfig("/config.properties");

        //Set up user handler
        userHandler = new UserHandler();

        //Load config from file
        System.out.println("Settings:\n");

        //Print device settings to console
        printSettings();

        //Initialize toPrint list.
        toPrint = new CopyOnWriteArrayList<>();

        //All threads
        allThreads = new CopyOnWriteArrayList<>();

        //Initialize IoT client
        if (!Config.offlineMode) {
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
                    IoTSensor sensor = sensorHandler.getSensorByTopic(s);
                    if (sensor != null) {
                        if (sensor.getStorage()) {
                            HavissIoT.storage.storeSensorValue(sensor, mqttMessage.toString());
                        }
                        sensor.updateValue(mqttMessage.toString());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    //No messages is delivered - should never be called
                }
            };
            client.setCallback(callback);

            try {
                //Initialize storage client
                storage = new IoTStorage(Config.databaseAddress, Config.databasePort, Config.database);
            } catch (MongoException e) {
                printMessage("Couldnt connect to database server");
            }
            //Objects for command handling
            SocketServer socketCommunication = new SocketServer(Config.serverPort, Config.numbOfClients);

            sensorHandler.loadFromFile();

            //Everything is started
            if(client.isConnected()) {
                printMessage("Application is ready");
            } else {
                System.out.println("Application not ready for use - Stopping");
                System.exit(1);
            }
        } else {
            printMessage("OFFLINE MODE! - No network connections");
            printMessage("Application is ready");
        }

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
    //print all current configurations
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
        System.out.println("Public IP address\t" + PublicIP.getPublicIP());
        try {
            System.out.println("Local IP address:\t" + InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.print("\n");
    }

    //Add new message and notify thread
    public static synchronized void printMessage(String message) {
        toPrint.add(message);
        synchronized (threadLock) {
            threadLock.notify();
        }
    }

    //Shutdown all threads and disconnects from all servers
    public static synchronized void exit(int status) {
        for(Thread t : allThreads) {
            try {
                t.interrupt();
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.disconnect();
        client.disconnect();
        System.out.println("Application is shutting down....");
        System.exit(status);
    }



}
