package net.haviss.havissIoT.Handlers;

import net.haviss.havissIoT.Sensors.IoTSensor;
import org.jetbrains.annotations.NotNull;
import net.haviss.havissIoT.Type.Location;

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

    //Get an array of sensors in a location

    public IoTSensor[] getSensorsByLocation(@NotNull Location location) {
        List<IoTSensor> sensors = new ArrayList<>();
        for(IoTSensor s : availableSensors) {
            if(s.getLocation() == location) {
                sensors.add(s);
            }
        }
        if(sensors.size() > 0) {
            return (IoTSensor[])sensors.toArray();
        } else {
            return null;
        }
    }

}
