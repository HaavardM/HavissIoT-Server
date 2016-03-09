package net.haviss.havissIoT.Device;

import net.haviss.havissIoT.Type.Room;

/**
 * Created by havar on 06.03.2016.
 */
public abstract class Device implements DeviceCallback {

    private String name, topic = null;
    private Room room = null;

    public Device(String name, String topic) {
        this.name = name;
        this.topic = topic;
        this.room = null;
    }

    public Device(String name, String topic, Room room) {
        this.name = name;
        this.topic = topic;
        this.room = room;
    }

    public String getTopic() {
        return topic;
    }

    public String getName() {
        return name;
    }

    public Room getRoom() {
        return room;
    }
}
