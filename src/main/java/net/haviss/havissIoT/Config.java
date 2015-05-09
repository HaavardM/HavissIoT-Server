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
    public static String brokerAddress;
    public static String clientID;
    public static String cmdTopic;
    public static String statusTopic;
    public static String databaseAddress;
    public static String database;
    public static int brokerPort;
    public static int qos;
    public static int databasePort;
    public static int serverPort;
    public static int numbOfClients;

    //Properties object to load from file
    private static Properties properties = new Properties();

    //Load new config
    public static void loadConfig(String configName) {
        try {
            properties.load(new BufferedReader(new InputStreamReader(Config.class.getClass().getResourceAsStream(configName))));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
