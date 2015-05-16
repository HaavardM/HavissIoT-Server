package net.haviss.havissIoT.Core;

import com.google.gson.Gson;
import net.haviss.havissIoT.Sensor.IoTSensor;
import org.bson.BSON;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by H�vard on 5/16/2015.
 */
public class SensorHandler {
    private CopyOnWriteArrayList<IoTSensor> availableSensors = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<String> sensorNames = new CopyOnWriteArrayList<>();

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


}
