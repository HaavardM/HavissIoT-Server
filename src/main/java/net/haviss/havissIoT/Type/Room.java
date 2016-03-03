package net.haviss.havissIoT.Type;

import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by havar on 21.02.2016.
 */
public class Room {
    private CopyOnWriteArrayList<IoTDevice> devices = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<IoTSensor> sensors = new CopyOnWriteArrayList<>();
    private String name;

    public Room(String name) {
        this.name = name;
    }

    public void addDevice(IoTDevice device) throws HavissIoTDeviceException {
        for(IoTDevice d : devices) {
            if(device.getName() == d.getName())
                throw new HavissIoTDeviceException("Device name already exist in room");
        }
        devices.add(device);
    }

    public IoTDevice getDeviceByName(String name) {
        for(IoTDevice d : devices) {
            if(d.getName() == name)
                return d;
        }
        //Returns null if no device found
        return null;
    }

    public IoTDevice getDeviceByTopic(String topic) {
        for(IoTDevice d : devices) {
            if(d.getTopic() == topic)
                return d;
        }
        //Returns null if no device found
        return null;
    }

    public String getName() {
        return this.name;
    }
}
