package net.haviss.havissIoT.Tools;


import net.haviss.havissIoT.Main;

import java.io.*;
import java.util.Properties;

/**
 * Created by HaavardM on 4/18/2015.
 */
public class Config {

    /*Config handler variables*/
    private static boolean isLoaded = false;
    /*Config values*/

    public static volatile String systemName = "";
    public static volatile String houseName = "";
    public static volatile String brokerAddress = "localhost";
    public static volatile String clientID = "";
    public static volatile String cmdTopic = "";
    public static volatile String statusTopic = "";
    public static volatile String databaseAddress = "localhost";
    public static volatile String database = "";
    public static volatile String sensorInfoCollection = "";
    public static volatile String sensorsCollection = "";
    public static volatile int brokerPort = 1883;
    public static volatile int qos = 0;
    public static volatile int databasePort = 0;
    public static volatile int serverPort = 23456;
    public static volatile int numbOfClients  = 10;
    public static volatile int keepAlive = 0;
    public static volatile int readTimeout = 0;
    public static volatile int refreshSensorTime  = 0;
    public static volatile boolean offlineMode = false;
    public static volatile boolean debugMode = false;
    public static volatile boolean enableLogging = false;
    public static volatile boolean enableVerbose = false;



    //Properties object to load from file
    private static Properties properties = new Properties();
    private static File file = new File("config.properties");
    public static void loadConfigFile(String configName) throws IOException {
        if(!file.exists()) {
            try {
                file.createNewFile();
                BufferedReader reader = new BufferedReader(new InputStreamReader(Config.class.getClass().getResourceAsStream(configName)));
                FileWriter writer = new FileWriter("config.properties");
                int c;
                if (reader.ready()) {
                    while ((c = reader.read()) != -1) {
                        writer.write(c);
                    }
                }
                writer.close();
                reader.close();
            } catch (IOException e) {
                Main.printMessage(e.getMessage()              );
            }
        }
        properties.load(new FileReader(file));
        loadConfig();
    }
    public static void loadExtConfigFile(String filePath) throws IOException {
        properties.load(new BufferedReader(new FileReader(filePath)));
        loadConfig();
        System.out.println(filePath + " config is loaded");
    }

    //Load new config
    public static void loadConfig() {
        if (properties != null) {
            try {
                systemName = getProperty("system_name");
                houseName = getProperty("house_name");
                brokerAddress = getProperty("broker_address");
                clientID = getProperty("client_id");
                cmdTopic = getProperty("cmd_topic");
                statusTopic = getProperty("status_topic");
                databaseAddress = getProperty("database_address");
                sensorInfoCollection = getProperty("sensor_info_collection");
                database = getProperty("database");
                brokerPort = Integer.parseInt(getProperty("broker_port"));
                qos = Integer.parseInt(getProperty("mqtt_qos"));
                databasePort = Integer.parseInt(getProperty("database_port"));
                serverPort = Integer.parseInt(getProperty("server_port"));
                numbOfClients = Integer.parseInt(getProperty("number_of_clients"));
                keepAlive = Integer.parseInt(getProperty("keep_alive"));
                readTimeout = Integer.parseInt(getProperty("read_timeout"));
                refreshSensorTime = Integer.parseInt(getProperty("refresh_sensor_time"));
                offlineMode = Boolean.parseBoolean(getProperty("offline_mode"));
                debugMode = Boolean.parseBoolean(getProperty("debug_mode"));
                enableLogging = Boolean.parseBoolean(getProperty("enable_logging"));
                enableVerbose = Boolean.parseBoolean(getProperty("enable_verbose"));
                sensorsCollection = getProperty("sensors_collection");
                isLoaded = true;
            } catch (NumberFormatException e) {
                Main.printMessage(e.getMessage());
            }
        } else {
            Main.printMessage("Properties couldn't be loaded");
        }

    }

    public static boolean propIsLoaded() {
        return isLoaded;
    }
    //Get property value
    public static String getProperty(String prop) {
        String property = properties.getProperty(prop);
        if(property == null)
            return "";
        return property;
    }

    //Set new property
    public static boolean setProperty(String prop, String value) throws IOException {
        if(properties.setProperty(prop, value) != null) {
            Config.loadConfig();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            properties.store(fileOutputStream, "Configuration file for HavissIoT");
            fileOutputStream.close();
            return true;
        }
        else {
            return false;
        }
    }
}
