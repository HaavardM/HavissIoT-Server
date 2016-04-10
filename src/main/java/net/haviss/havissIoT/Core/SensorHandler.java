package net.haviss.havissIoT.Core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.Exceptions.HavissIoTSensorException;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Sensor.IoTSensor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Timer;

/**
 * Created by H�vard on 5/16/2015.
 * Handles all sensors
 */
public class SensorHandler {
    private CopyOnWriteArrayList<IoTSensor> availableSensors = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<String> sensorNames = new CopyOnWriteArrayList<>();
    private Timer timer;

    public SensorHandler() {
        //Check if sensors are inactive each x seconds (se config.properties)
         timer = new Timer(Config.refreshSensorTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(IoTSensor s : availableSensors) {
                    s.checkActive();
                }
            }
        });
        timer.start();
    }

    //Add new sensor
    public synchronized void addSensor(String name, String topic, String type, boolean toStore, long timeout) throws HavissIoTSensorException{
        if (sensorNames.contains(name)) {
            throw new HavissIoTSensorException("Sensor already exist");
        } else {
            availableSensors.add(new IoTSensor(name, topic, type, toStore, timeout ));
            sensorNames.add(name);
            try {
                Main.client.subscribeToTopic(topic, Config.qos);
            } catch (HavissIoTMQTTException e) {
                Main.printMessage(e.getMessage());
            }
            this.writeToFile();
        }
    }

    //Remove sensor by name
    public void removeSensorByName(String name) throws HavissIoTSensorException {
        boolean sensorRemoved = false;
        for(IoTSensor s : availableSensors) {
            if(name.compareTo(s.getName()) == 0) {
                availableSensors.remove(s);
                sensorNames.remove(s.getName());
                try {
                    Main.client.unsubscribeToTopic(s.getTopic());
                    sensorRemoved = true;
                } catch (HavissIoTMQTTException e) {
                    Main.printMessage(e.getMessage());
                }
                break;
            }
        }
        if(!sensorRemoved) {
            throw new HavissIoTSensorException("Sensor not found");
        }
        this.writeToFile();
    }

    //Remove sensor by topic
    public void removeSensorByTopic(String topic) {
        for(IoTSensor s : availableSensors) {
            if(topic.compareTo(s.getTopic()) == 0) {
                availableSensors.remove(s);
                sensorNames.remove(s.getName());
                try {
                    Main.client.unsubscribeToTopic(s.getTopic());
                } catch (HavissIoTMQTTException e) {
                    Main.printMessage(e.getMessage());
                }
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
        //Setting up parser
        JsonParser parser = new JsonParser();
        //New json array
        JsonArray jsonArray;
        try {
            //Load json array from file
            jsonArray = parser.parse(new FileReader("sensors.json")).getAsJsonArray();
        } catch (IOException e) {
            jsonArray = null;
        }
        //If jsonarray successfully loaded from file
        if(jsonArray != null) {
            //Load object from jsonarray
            try {
                for (int i = 0; i < jsonArray.size(); i++) {
                    String name = jsonArray.get(i).getAsJsonObject().get("name").getAsString();
                    String topic = jsonArray.get(i).getAsJsonObject().get("topic").getAsString();
                    String type = jsonArray.get(i).getAsJsonObject().get("type").getAsString();
                    boolean toStore = jsonArray.get(i).getAsJsonObject().get("name").getAsBoolean();
                    long timeout = jsonArray.get(i).getAsJsonObject().get("timeout").getAsLong();
                    availableSensors.add(new IoTSensor(name, topic, type, toStore, timeout));
                }
            } catch (JsonParseException e) {
                Main.printMessage("Json parse error: " + e.getMessage());
            }
        }
    }
}
