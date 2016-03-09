package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.Device.Device;
import net.haviss.havissIoT.Type.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by haavard on 06.03.2016.
 * Handles all available devices for the system.
 */
public class DeviceHandler {

    private CopyOnWriteArrayList<Device> availableDevices = new CopyOnWriteArrayList<>();

    public Device getDeviceByName(String name) {
        for(Device d : availableDevices) {
            if(d.getName() == name)
                return d;
        }
        return null;
    }

    public Device getDeviceByTopic(String topic) {
        for(Device d : availableDevices) {
            if(d.getTopic() == topic)
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

    public void deliverMessage(String topic, String message) {
        getDeviceByTopic(topic).messageArrived(topic, message);
    }


}
