package net.haviss.havissIoT.Handlers;

import org.jetbrains.annotations.NotNull;
import net.haviss.havissIoT.Sensors.Sensor;
import net.haviss.havissIoT.Type.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by havar on 9/6/2015.
 */
public class SensorHandler {

    private CopyOnWriteArrayList<Sensor> availableSensors = new CopyOnWriteArrayList<>();

    //Get sensor by knowing the name
    public Sensor getSensorByName(String name) {
        for(Sensor s : availableSensors) {
            if(s.getName().compareTo(name) == 0) {
                return s;
            }
        }
        return null;
    }

    //Get a sensor by knowing the topic
    public Sensor getSensorByTopic(String topic) {
        for(Sensor s : availableSensors) {
            if(s.getTopic().compareTo(topic) == 0) {
                return s;
            }
        }
        return null;
    }

    //Get an array of sensors in a location

    public Sensor[] getSensorsByLocation(@NotNull Location location) {
        List<Sensor> sensors = new ArrayList<>();
        for(Sensor s : availableSensors) {
            if(s.getLocation() == location) {
                sensors.add(s);
            }
        }
        if(sensors.size() > 0) {
            return (Sensor[])sensors.toArray();
        } else {
            return null;
        }
    }

}
