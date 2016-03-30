package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.Device.Device;
import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;
import net.haviss.havissIoT.Type.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by haavard on 06.03.2016.
 * Handles all available devices for the system.
 */
public class DeviceHandler {

    private CopyOnWriteArrayList<Device> availableDevices = new CopyOnWriteArrayList<>();

    public DeviceHandler() {
    }

    public Device getDeviceByName(String name) {
        for(Device d : availableDevices) {
            if(d.getName().compareTo(name) == 0)
                return d;
        }
        return null;
    }

    public Device getDeviceByTopic(String topic) {
        for(Device d : availableDevices) {
            if(d.getTopic().compareTo(topic) == 0)
                return d;
        }
        return null;
    }

    public Device[] getDevicesByRoom(Room room) {
        List<Device> returnList = new ArrayList<>();
        for(Device d : availableDevices) {
            if(d.getRoom() == room)
                returnList.add(d);
        }
        if(returnList.size() > 0)
            return (Device[])returnList.toArray();
        else
            return null;
    }

    public void deliverMessage(String topic, String message) throws HavissIoTDeviceException {
        getDeviceByTopic(topic).messageArrived(topic, message);
    }


}