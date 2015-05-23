package net.haviss.havissIoT.Core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Sensor.IoTSensor;
import org.json.simple.JSONArray;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by H�vard on 5/16/2015.
 * Handles all sensors
 */
public class SensorHandler {
    private CopyOnWriteArrayList<IoTSensor> availableSensors = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<String> sensorNames = new CopyOnWriteArrayList<>();

    //Constructor
    public SensorHandler() {
        loadFromFile();
    }

    //Add new sensor
    public synchronized boolean addSensor(String name, String topic, String type, boolean toStore) {
        if (sensorNames.contains(name)) {
            return false;
        } else {
            availableSensors.add(new IoTSensor(name, topic, type, toStore));
            sensorNames.add(name);
            HavissIoT.client.subscribeToTopic(topic, Config.qos);
            this.writeToFile();
            return true;
        }
    }

    //Remove sensor
    public void removeSensor(String name) {
        for(IoTSensor s : availableSensors) {
            if(name.compareTo(s.getName()) == 0) {
                availableSensors.remove(s);
                sensorNames.remove(name);
                HavissIoT.client.unsubscribeToTopic(s.getTopic());
            }
        }
        this.writeToFile();
    }

    //Get sensor object with name
    public synchronized IoTSensor getSensorByName(String name) {
        for(IoTSensor s : availableSensors) {
            if(name.compareTo(s.getName()) == 0) {
                return s;
            }
        }
        return null;
    }

    //Get sensor object with name
    public synchronized IoTSensor getSensorByTopic(String topic) {
        for(IoTSensor s : availableSensors) {
            if(topic.compareTo(s.getName()) == 0) {
                return s;
            }
        }
        return null;
    }

    //Get list of sensors
    public synchronized CopyOnWriteArrayList<IoTSensor> getSensorsList() {
        return availableSensors;
    }

    public synchronized void writeToFile() {
        try {
            PrintWriter writer = new PrintWriter("sensors.json",  "UTF-8");
            writer.write(new Gson().toJson(availableSensors));
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //Load sensors from file
    public synchronized void loadFromFile() {
        //Setting up parser
        JSONParser parser = new JSONParser();
        //New json array
        JSONArray jsonArray;
        try {
            //Load json array from file
            jsonArray = (JSONArray) parser.parse(new FileReader("sensors.json"));
        } catch (IOException | ParseException e) {
            jsonArray = null;
        }
        //If jsonarray successfully loaded from file
        if(jsonArray != null) {
            //Foreach object in jsonarray - load properties
            for(Object o : jsonArray) {
                JSONObject sensor = (JSONObject) o;
                String sensorName = (String) sensor.get("name");
                String sensorType = (String) sensor.get("type");
                String sensorTopic = (String) sensor.get("topic");
                boolean sensorStorage = (boolean) sensor.get("storage");
                //Add new sensor to list with appropriate parameters
                addSensor(sensorName, sensorTopic, sensorType, sensorStorage);
            }
        }
    }
}
