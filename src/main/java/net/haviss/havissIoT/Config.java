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
    public static volatile String brokerAddress;
    public static volatile String clientID;
    public static volatile String cmdTopic;
    public static volatile String statusTopic;
    public static volatile String databaseAddress;
    public static volatile String database;
    public static volatile String witAddress;
    public static volatile String witToken;
    public static volatile int brokerPort;
    public static volatile int qos;
    public static volatile int databasePort;
    public static volatile int serverPort;
    public static volatile int numbOfClients;
    public static volatile int keepAlive;
    public static volatile int readTimeout;
    public static volatile int refreshSensorTime;
    public static volatile int refreshSubscriptionTime;
    public static volatile boolean offlineMode;



    //Properties object to load from file
    private static Properties properties = new Properties();

    //Load new config
    public static void loadConfig(String configName) {
        try {
            properties.load(new BufferedReader(new InputStreamReader(Config.class.getClass().getResourceAsStream(configName))));
            brokerAddress = getProperty("broker_address");
            clientID = getProperty("client_id");
            cmdTopic = getProperty("cmd_topic");
            statusTopic = getProperty("status_topic");
            databaseAddress = getProperty("database_address");
            database = getProperty("database");
            brokerPort = Integer.parseInt(getProperty("broker_port"));
            qos = Integer.parseInt(getProperty("mqtt_qos"));
            databasePort = Integer.parseInt(getProperty("database_port"));
            serverPort = Integer.parseInt(getProperty("server_port"));
            numbOfClients = Integer.parseInt(getProperty("number_of_clients"));
            keepAlive = Integer.parseInt(getProperty("keep_alive"));
            readTimeout = Integer.parseInt(getProperty("read_timeout"));
            refreshSensorTime = Integer.parseInt(getProperty("refresh_sensor_time"));
            refreshSubscriptionTime = Integer.parseInt(getProperty("refresh_subscription_time"));
            witAddress = getProperty("wit_address");
            witToken = getProperty("wit_token");
            offlineMode = Boolean.parseBoolean(getProperty("offline_mode"));
        } catch (IOException | NumberFormatException e) {
            HavissIoT.printMessage(e.getMessage());
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
