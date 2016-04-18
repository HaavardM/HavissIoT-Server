package net.haviss.havissIoT;

import com.mongodb.async.client.MongoClient;
import net.haviss.havissIoT.Communication.MQTTClient;
import net.haviss.havissIoT.Communication.SocketServer;
import net.haviss.havissIoT.Core.DeviceHandler;
import net.haviss.havissIoT.Core.UserHandler;
import net.haviss.havissIoT.Device.Devices.TestDataLogger;
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
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by HaavardM on 5/3/2015.
 * Main class
 */
public class Main {



    //<editor-fold desc="OBJECTS">
    public static MQTTClient client;
    public static UserHandler userHandler;
    public static SocketServer socketServer;
    public static DeviceHandler deviceHandler = new DeviceHandler();
    public static final Object threadLock = new Object();
    public static CopyOnWriteArrayList<Thread> allThreads;
    private static CopyOnWriteArrayList<String> toPrint;
    //</editor-fold>

    //Main method
    public static void main(String args[]) {

        System.out.println("\nhavissIoT server\n");
        //Read arguments from args array
        //<editor-fold desc="Program parameters">
        HashMap<String, String> arguments = new HashMap<>();
        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("-")) {
                if((i+1) < args.length && !args[i+1].startsWith("-")) {
                    arguments.put(args[i], args[i+1]);
                } else {
                    arguments.put(args[i], null);
                }
            }
        }
        for(String s : arguments.keySet()) {
            switch (s) {
                case "-c":
                    //<editor-fold desc="Alternate config file">
                    String filePath = arguments.get(s);
                    if(filePath != null)
                        try {
                            Config.loadExtConfigFile(filePath);
                        } catch (IOException e) {
                            //TODO: Better error handling
                            e.printStackTrace();
                        }
                    //</editor-fold>

            }
        }
        //</editor-fold>
        //Load config file if not done already
        //<editor-fold desc="Load config">
        try {
            if(!Config.propIsLoaded())
                Config.loadConfigFile("/config.properties");
        } catch (IOException e) {
            //TODO: Better error handling here
            e.printStackTrace();
        }
        //</editor-fold>
        //Initialize neccesary objects
        //<editor-fold desc="Initialize handlers and objects">
        //Set up user handler
        userHandler = new UserHandler();
        //Initialize toPrint list.
        toPrint = new CopyOnWriteArrayList<>();
        //All threads
        allThreads = new CopyOnWriteArrayList<>();

        deviceHandler.addDevice(new TestDataLogger("dataLogger", "haviss/house/datalogger"));
        deviceHandler.addDevice(new TestDataLogger("dataLogger1", "haviss/house/datalogger1"));
        deviceHandler.addDevice(new TestDataLogger("dataLogger2", "haviss/house/datalogger2"));
        deviceHandler.addDevice(new TestDataLogger("dataLogger3", "haviss/house/datalogger3"));
        deviceHandler.addDevice(new TestDataLogger("dataLogger4", "haviss/house/datalogger4"));
        deviceHandler.addDevice(new TestDataLogger("dataLogger5", "haviss/house/datalogger5"));
        deviceHandler.addDevice(new TestDataLogger("dataLogger6", "haviss/house/datalogger6"));
        deviceHandler.addDevice(new TestDataLogger("dataLogger7", "haviss/house/datalogger7"));

        //</editor-fold>
        //Initialize IoT client
        //<editor-fold desc="IoTClient">
        if (Config.offlineMode) {
            printMessage("OFFLINE MODE! - No network connections");
            printMessage("Application is ready");
        } else {
            client = new MQTTClient(Config.clientID);
            client.connect(Config.brokerAddress, Config.brokerPort);
            socketServer = new SocketServer(Config.serverPort, Config.numbOfClients);

            //Setting up new callback for client
            MqttCallback callback = new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                     Main.printMessage("MQTT broker connection lost: " + throwable.getMessage());
                    client.disconnect();
                    client = new MQTTClient(Config.clientID);
                    client.connect(Config.brokerAddress, Config.brokerPort);
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    //TODO: Analyze message
                    deviceHandler.deliverMessage(s, mqttMessage.toString());

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    //TODO: Use for critical device commands


                }
            };
            client.setCallback(callback);
            //Everything is started
            if(client.isConnected()) {
                printMessage("Application is ready");
            } else {
                System.out.println("Application not ready for use - Stopping");
                System.exit(1);
            }
        }
        //</editor-fold>
        //Initalize log file if needed
        //<editor-fold desc="Logging">
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
        //</editor-fold>
        //Print device settings to console
        printSettings();
        //Application must run forever

        //<editor-fold desc="LOGGER">
        while(!Thread.currentThread().isInterrupted()) {
            //If there is something to print
            if(toPrint.size() > 0) {
                for(String s : toPrint) {
                    String toWrite = (new Date().toString() + " " + s);
                    if(Config.enableVerbose)
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
        //</editor-fold>
    }
    //print all current configurations
    public static void printSettings() {
        System.out.println("Settings:\n");
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
        if(!Config.offlineMode)
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
