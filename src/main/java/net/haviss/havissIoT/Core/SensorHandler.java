package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.Type.IoTSensor;
import net.haviss.havissIoT.Type.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by havar on 9/6/2015.
 */
public class SensorHandler {

    private CopyOnWriteArrayList<IoTSensor> availableSensors = new CopyOnWriteArrayList<>();

    //Get sensor by knowing the name
    public IoTSensor getSensorByName(String name) {
        for(IoTSensor s : availableSensors) {
            if(s.getName().compareTo(name) == 0) {
                return s;
            }
        }
        return null;
    }

    //Get a sensor by knowing the topic
    public IoTSensor getSensorByTopic(String topic) {
        for(IoTSensor s : availableSensors) {
            if(s.getTopic().compareTo(topic) == 0) {
                return s;
            }
        }
        return null;
    }

    //Get an array of sensors in a room
    /*
    public IoTSensor[] getSensorsByRoom(Room room) {
        List<IoTSensor> sensors = new ArrayList<>();
        for(IoTSensor s : availableSensors) {
            if(s.getRoom() == room) {
                sensors.add(s);
            }
        }
        if(sensors.size() > 0) {
            return (IoTSensor[])sensors.toArray();
        } else {
            return null;
        }
    }
    */

}
