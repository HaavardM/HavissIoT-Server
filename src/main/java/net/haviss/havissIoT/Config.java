package net.haviss.havissIoT;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by HaavardM on 4/18/2015.
 */
public class Config {

    /*Config values*/

    public static volatile String systemName;
    public static volatile String houseName;
    public static volatile String brokerAddress;
    public static volatile String clientID;
    public static volatile String cmdTopic;
    public static volatile String statusTopic;
    public static volatile String databaseAddress;
    public static volatile String database;
    public static volatile String witAddress;
    public static volatile String witToken;
    public static volatile String sensorInfoCollection;
    public static volatile int brokerPort;
    public static volatile int qos;
    public static volatile int databasePort;
    public static volatile int serverPort;
    public static volatile int numbOfClients;
    public static volatile int keepAlive;
    public static volatile int readTimeout;
    public static volatile int refreshSensorTime;
    public static volatile long sensorTimeout;
    public static volatile boolean offlineMode;
    public static volatile boolean debugMode;
    public static volatile boolean enableLogging;



    //Properties object to load from file
    private static Properties properties = new Properties();

    //Load new config
    public static void loadConfig(String configName) {
        try {
            properties.load(new BufferedReader(new InputStreamReader(Config.class.getClass().getResourceAsStream(configName))));
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
            sensorTimeout = Long.parseLong(getProperty("sensor_timeout"));
            witAddress = getProperty("wit_address");
            witToken = getProperty("wit_token");
            offlineMode = Boolean.parseBoolean(getProperty("offline_mode"));
            debugMode = Boolean.parseBoolean(getProperty("debug_mode"));
            enableLogging = Boolean.parseBoolean(getProperty("enable_logging"));
        } catch (IOException | NumberFormatException e) {
            Main.printMessage(e.getMessage());
        }

    }

    //Get property value
    public static String getProperty(String prop) {
        return properties.getProperty(prop);
    }

    //Set new property
    public static void setProperty(String prop, String value) {
        properties.setProperty(prop, value);
    }
}
