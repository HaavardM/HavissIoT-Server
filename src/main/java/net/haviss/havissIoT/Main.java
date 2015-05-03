package net.haviss.havissIoT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String args[]) {
        //Database settings
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver"); //Do not disturb console with unnecessary information
        mongoLogger.setLevel(Level.WARNING);

        //Objects
        Properties prop = new Properties();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter command: ");
            String text = scanner.nextLine();
            text = text.toLowerCase();
            if (text.length() > 0) {
                String[] commands = text.split("\\s+");
                if(commands[0].compareTo("help") == 0) ConsoleCommands.printHelp();
                else if (commands[0].compareTo("exit") == 0) System.exit(0);
                else if(commands[0].compareTo("add") == 0) {
                    for(int i = 1; i < commands.length; i++) {
                        havissIoT.client.subscribeToTopic(commands[i], havissIoT.qos);
                        havissIoT.client.topics.add(commands[i]);
                    }
                    System.out.println("Subscribed to " + Integer.toString(commands.length - 1) + " topics");
                } else if(commands[0].compareTo("topics") == 0) {
                    System.out.println("\nAvailable topics (" + havissIoT.client.topics.size() + "):");
                    for (String s : havissIoT.client.topics ) {
                        System.out.println(s);
                    }
                } else if(commands[0].compareTo("set") == 0) {
                    if(commands.length  > 3) {
                        System.out.println("Too many arguments");
                    } else if(commands.length < 3) {
                        System.out.println("Too few arguments");
                    } else if(commands.length == 3) {
                        havissIoT.config.setProperty(commands[1], commands[2]);
                        System.out.println("Properties set, restart to enable changes");
                    }
                }
            }
        }
    }
}
