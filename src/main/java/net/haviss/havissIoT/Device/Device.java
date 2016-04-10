package net.haviss.havissIoT.Device;

import net.haviss.havissIoT.Sensors.IoTSensor;
import net.haviss.havissIoT.Type.DeviceType;
import net.haviss.havissIoT.Type.IoTDataType;
import net.haviss.havissIoT.Type.Room;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by havar on 06.03.2016.
 */
public abstract class Device implements DeviceCallback {

    protected CopyOnWriteArrayList<IoTSensor> availableSensors = new CopyOnWriteArrayList<>();
    private String name, topic = null;
    private Room room = null;
    private DeviceType deviceType = DeviceType.None;
    private IoTDataType dataType = IoTDataType.String;

    public Device(String name, String topic, DeviceType deviceType, IoTDataType dataType) {
        this.name = name;
        this.topic = topic;
        this.room = null;
        this.dataType = dataType;
        this.deviceType = deviceType;
    }




    public Device(String name, String topic, DeviceType deviceType, IoTDataType dataType, Room room) {
        this.name = name;
        this.topic = topic;
        this.room = room;
        this.dataType = dataType;
        this.deviceType = deviceType;
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

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public IoTDataType getDataType() {
        return dataType;
    }

    public IoTSensor[] getSensors() {
        IoTSensor[] sensors = new IoTSensor[availableSensors.size()];
        availableSensors.toArray(sensors);
        return  sensors;
    }

    public String getSubTopic(String topic) {
        if(topic.contains(this.topic)) {
            return topic.replace(getTopic(), "");
        }
        else
            return null;
    }
}
