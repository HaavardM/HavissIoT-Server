package net.haviss.havissIoT;

import net.haviss.havissIoT.ApplicationCommands.ApplicationCommandHandler;
import net.haviss.havissIoT.Communication.MQTTClient;
import net.haviss.havissIoT.Communication.ServerCommunication.SocketServer;
import net.haviss.havissIoT.Device.Devices.ToggleDevice;
import net.haviss.havissIoT.Device.IoTDevice;
import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;
import net.haviss.havissIoT.Handlers.DeviceHandler;
import net.haviss.havissIoT.Handlers.UserHandler;
import net.haviss.havissIoT.Device.Devices.TestDataLogger;
import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.External.PublicIP;
import net.haviss.havissIoT.Sensors.IoTSensor;
import net.haviss.havissIoT.Tools.Config;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by HaavardM on 5/3/2015.
 * Main class
 */
public class Main implements Daemon {
    
    //<editor-fold desc="OBJECTS">
    public static MQTTClient client;
    public static UserHandler userHandler;
    public static SocketServer socketServer;
    public static DeviceHandler deviceHandler = new DeviceHandler();
    public static final Object threadLock = new Object();
    public static CopyOnWriteArrayList<Thread> allThreads = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<String> toPrint = new CopyOnWriteArrayList<>();
    private static Random rnd = new Random();
    private static String enterCommandString = "Enter command: ";
    private static boolean isRunningDaemon = false;
    private static boolean isRunning = true;
    //</editor-fold>

    //Main method
    public static void main(String args[]) {

        //<editor-fold desc="Load config">
        try {
            if(!Config.propIsLoaded())
                Config.loadConfigFile("/config.properties");
        } catch (IOException e) {
            //TODO: Better error handling here
            e.printStackTrace();
        }
        //</editor-fold>

        if(Config.enableVerbose)
            System.out.println("\nhavissIoT server\n");
        //Read arguments from args array
        //<editor-fold desc="Program parameters">
        HashMap<String, String> arguments = new HashMap<>();
        if(args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-")) {
                    if ((i + 1) < args.length && !args[i + 1].startsWith("-")) {
                        arguments.put(args[i], args[i + 1]);
                    } else {
                        arguments.put(args[i], null);
                    }
                }
            }
            for (String s : arguments.keySet()) {
                switch (s) {
                    case "-c":
                        //<editor-fold desc="Alternate config file">
                        String filePath = arguments.get(s);
                        if (filePath != null)
                            try {
                                Config.loadExtConfigFile(filePath);
                            } catch (IOException e) {
                                //TODO: Better error handling
                                e.printStackTrace();
                            }
                        //</editor-fold>

                }
            }
        }
        //</editor-fold>
        //Load config file if not done already

        //Initialize neccesary objects
        //<editor-fold desc="Initialize handlers and objects">
        //Set up user handler
        userHandler = new UserHandler();
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
                System.out.println("ERROR: MQTT not connected");
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
                fileWriter = new FileOutputStream(logFile, false);
            } catch (IOException e) {
                e.printStackTrace();
                fileWriter = null;
            }
        }
        //</editor-fold>
        //Print device settings to console
        if(Config.enableVerbose && !isRunningDaemon)
            printSettings();

        //<editor-fold desc="TEST">
        deviceHandler.addDevice(new ToggleDevice("Nightstand lamp", "bedroom/nightlamp"));
        Timer t = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(client.isConnected()) {
                    for(IoTDevice d : deviceHandler.getAllDevices()) {
                        if(d instanceof ToggleDevice) {
                            ((ToggleDevice) d).toggle();
                        }
                    }
                }
            }
        });
        t.start();
        //</editor-fold>
        //Application must run forever

        Scanner reader = new Scanner(System.in);
        ApplicationCommandHandler cmdHandler = new ApplicationCommandHandler();
        //<editor-fold desc="LOOP">
        while(!Thread.currentThread().isInterrupted() && isRunning) {
            try {
                if(System.in.available() > 0 && !isRunningDaemon) {
                    String cmd = reader.nextLine();
                    String[] cmds = cmd.split("\\s+");
                    String[] parameters = new String[cmds.length - 1];
                    System.arraycopy(cmds, 1, parameters, 0, cmds.length - 1);
                    System.out.println(cmdHandler.processCommand(cmds[0], parameters));
                }
            } catch (IOException e) {
                printMessage(e.getMessage());
            }
            //If there is something to print
            if(toPrint.size() > 0) {
                ArrayList<String> toRemove = new ArrayList<>();
                for(String s : toPrint) {
                    String toWrite = (new Date().toString() + " " + s);
                    if(Config.enableVerbose && !isRunningDaemon)
                        System.out.println(toWrite); //Printing to console with
                    if(fileWriter != null && Config.enableLogging) {
                        try {
                            fileWriter.write((toWrite + "\n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    toRemove.add(s);
                }
                for(String s : toRemove) {
                    toPrint.remove(s);
                }
            }
        }
        //</editor-fold>
    }

    public static void reloadCommunicationObjects() {
        if(client.isConnected()) {
            client.disconnect();
        }

        if(!Config.offlineMode) {
            client = new MQTTClient(Config.clientID);
            client.connect(Config.brokerAddress, Config.brokerPort);
            try {
                socketServer.stopThread();
                socketServer = new SocketServer(Config.serverPort, Config.numbOfClients);
            } catch (IOException e) {
                printMessage(e.getMessage());
            }
        } else {
            printMessage("OFFLINE MODE! - No network connections");
        }


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


    @Override
    public void init(DaemonContext daemonContext) throws DaemonInitException, Exception {

    }

    @Override
    public void start() throws Exception {
        isRunningDaemon = true;
        main(null);
        System.out.println("HavissIoT is starting");
    }

    @Override
    public void stop() throws Exception {
        isRunning = false;
        System.exit(0);
    }

    @Override
    public void destroy() {
        System.exit(1);
    }
}
