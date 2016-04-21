package net.haviss.havissIoT;


import java.io.*;
import java.util.Properties;

/**
 * Created by HaavardM on 4/18/2015.
 */
public class Config {

    /*Config handler variables*/
    private static boolean isLoaded = false;
    /*Config values*/

    public static volatile String systemName;
    public static volatile String houseName;
    public static volatile String brokerAddress;
    public static volatile String clientID;
    public static volatile String cmdTopic;
    public static volatile String statusTopic;
    public static volatile String databaseAddress;
    public static volatile String database;
    public static volatile String sensorInfoCollection;
    public static volatile String sensorsCollection;
    public static volatile int brokerPort = 1883;
    public static volatile int qos;
    public static volatile int databasePort;
    public static volatile int serverPort;
    public static volatile int numbOfClients;
    public static volatile int keepAlive;
    public static volatile int readTimeout;
    public static volatile int refreshSensorTime;
    public static volatile boolean offlineMode;
    public static volatile boolean debugMode;
    public static volatile boolean enableLogging;
    public static volatile boolean enableVerbose;



    //Properties object to load from file
    private static Properties properties = new Properties();

    public static void loadConfigFile(String configName) throws IOException {
        File file = new File("config.properties");
        if(!file.exists()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Config.class.getClass().getResourceAsStream(configName)));
            FileWriter writer = new FileWriter("config.properties");
            int c;
            if(reader.ready()) {
                while ((c = reader.read()) != -1) {
                    writer.write(c);
                }
            }
            writer.close();
            properties.load(reader);
            reader.close();
        }
        else {
            properties.load(new FileReader(file));
        }
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
            Main.printMessage("Properties couldt be loaded");
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
    public static void setProperty(String prop, String value) {
        properties.setProperty(prop, value);
    }
}
