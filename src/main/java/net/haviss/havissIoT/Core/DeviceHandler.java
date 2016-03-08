package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.Devices.Device;
import net.haviss.havissIoT.Type.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by havar on 06.03.2016.
 */
public class DeviceHandler {

    private CopyOnWriteArrayList<Device> availableSensors = new CopyOnWriteArrayList<>();

    public Device getDeviceByName(String name) {
        for(Device d : availableSensors) {
            if(d.getName() == name)
                return d;
        }
        return null;
    }

    public Device getDeviceByTopic(String topic) {
        for(Device d : availableSensors) {
            if(d.getTopic() == topic)
                return d;
        }
        return null;
    }

    public Device[] getDevicesByRoom(Room room) {
        List<Device> returnList = availableSensors.stream().filter(d -> d.getRoom() == room).collect(Collectors.toList());
        if(returnList.size() > 0)
            return (Device[])returnList.toArray();
        else
            return null;
    }


}
