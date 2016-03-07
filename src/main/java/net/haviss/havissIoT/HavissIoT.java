package net.haviss.havissIoT;

import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import net.haviss.havissIoT.Communication.APIServer;
import net.haviss.havissIoT.Communication.MQTTClient;
import net.haviss.havissIoT.Core.UserHandler;
import net.haviss.havissIoT.Exceptions.HavissIoTHttpException;
import net.haviss.havissIoT.External.PublicIP;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public static MQTTClient client;
    public static MongoClient mongoClient;
    public static UserHandler userHandler;
    public static final Object threadLock = new Object();
    public static CopyOnWriteArrayList<Thread> allThreads;
    private static CopyOnWriteArrayList<String> toPrint;


    //Main method
    public static void main(String args[]) {

        //Load logger and config

        System.out.println("\nhavissIoT server\n");

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
        if (Config.offlineMode) {
            printMessage("OFFLINE MODE! - No network connections");
            printMessage("Application is ready");
        } else {
            client = new MQTTClient(Config.clientID);
            client.connect(Config.brokerAddress, Config.brokerPort);

            //Setting up new callback for client
            MqttCallback callback = new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    HavissIoT.printMessage("MQTT broker connection lost: " + throwable.getMessage());
                    client.disconnect();
                    client = new MQTTClient(Config.clientID);
                    client.connect(Config.brokerAddress, Config.brokerPort);
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    //TODO: Analyze message
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    //No messages is delivered - should never be called

                }
            };
            client.setCallback(callback);

            try {
                mongoClient = MongoClients.create(new ConnectionString("mongodb://" + Config.databaseAddress));
            } catch (MongoException e) {
                printMessage(e.getMessage());
            }
            //Everything is started
            if(client.isConnected()) {
                printMessage("Application is ready");
            } else {
                System.out.println("Application not ready for use - Stopping");
                System.exit(1);
            }
        }

        FileOutputStream fileWriter = null;
        if(Config.enableLogging) {
            File logFile = new File("log.txt");
            try {
                logFile.createNewFile();
                fileWriter = new FileOutputStream(logFile, true);
            } catch (IOException e) {
                e.printStackTrace();
                fileWriter = null;
            }
        }

        //Application must run forever
        while(!Thread.currentThread().isInterrupted()) {
            //If there is something to print
            if(toPrint.size() > 0) {
                int indexReached = 0;
                for(String s : toPrint) {
                    String toWrite = (new Date().toString() + " " + s);
                    System.out.println(toWrite); //Printing to console with
                    if(fileWriter != null && Config.enableLogging) {
                        try {
                            fileWriter.write((toWrite + "\n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    toPrint.remove(s);
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
        System.out.println("MQTT broker settings:");
        System.out.println("Broker address:\t " + Config.brokerAddress);
        System.out.println("Broker port:\t" + Integer.toString(Config.brokerPort));
        System.out.println("Client id:\t" + Config.clientID);
        System.out.println("QOS:\t" + Integer.toString(Config.qos));
        System.out.println("\nDatabase settings:");
        System.out.println("Database address:\t" + Config.databaseAddress);
        System.out.println("Database port:\t" + Integer.toString(Config.databasePort));
        System.out.println("Database:\t" + Config.database);
        System.out.println("\nServer settings:");
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
    //TODO: Not working properly
    public static synchronized void exit(int status) {
        for(Thread t : allThreads) {
            try {
                t.interrupt();
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        client.disconnect();
        System.out.println("Application is shutting down....");
        System.exit(status);
    }



}
