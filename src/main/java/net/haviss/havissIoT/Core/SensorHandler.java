package net.haviss.havissIoT.Core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.haviss.havissIoT.Sensor.IoTSensor;
import org.json.simple.JSONArray;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Håvard on 5/16/2015.
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
            return true;
        }
    }

    //Remove sensor
    public void removeSensor(String name) {
        for(IoTSensor s : availableSensors) {
            if(name.compareTo(s.getName()) == 0) {
                availableSensors.remove(s);
                sensorNames.remove(name);
            }
        }
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
        JSONParser parser = new JSONParser();
        JSONArray jsonArray;
        try {
            jsonArray = (JSONArray) parser.parse(new FileReader("sensors.json"));
        } catch (IOException | ParseException e) {
            jsonArray = null;
        }
        if(jsonArray != null) {
            for(Object o : jsonArray) {
                JSONObject sensor = (JSONObject) o;
                String sensorName = (String) sensor.get("name");
                String sensorType = (String) sensor.get("type");
                String sensorTopic = (String) sensor.get("topic");
                String storageString = (String) sensor.get("storage");
                boolean sensorStorage = Boolean.parseBoolean(storageString);
                addSensor(sensorName, sensorTopic, sensorType, sensorStorage);
            }
        }
    }
}
