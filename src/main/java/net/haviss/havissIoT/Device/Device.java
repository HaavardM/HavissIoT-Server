package net.haviss.havissIoT.Device;

import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;
import net.haviss.havissIoT.Sensors.Sensor;
import net.haviss.havissIoT.Type.DeviceType;
import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.Location;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by havar on 06.03.2016.
 */
public abstract class Device implements DeviceCallback {

    public static Device createDevice(String name, String topic, DeviceType type, DataType dataType) {
        Device d = new Device(name, topic, type, dataType) {
            @Override
            public void messageArrived(String topic, String message) throws HavissIoTDeviceException {
                //Do nothing
            }

            @Override
            public void messageDelivered(String topic) {
                //Do nothing
            }
        };
        return d;
    }
    protected CopyOnWriteArrayList<Sensor> availableSensors = new CopyOnWriteArrayList<>();
    private String name, topic = null;
    private Location room = null;
    private DeviceType deviceType = DeviceType.None;
    private DataType dataType = DataType.String;

    //<editor-fold desc="Constructors">
    public Device(String name, String topic, DeviceType deviceType, DataType dataType) {
        this.name = name;
        this.topic = topic;
        this.room = null;
        this.dataType = dataType;
        this.deviceType = deviceType;
    }

    public Device(String name, String topic, DeviceType deviceType, DataType dataType, Location room) {
        this.name = name;
        this.topic = topic;
        this.room = room;
        this.dataType = dataType;
        this.deviceType = deviceType;
    }
    //</editor-fold>
    // Get the subtopic
    public String getSubTopic(String topic) {
        if(topic.contains(this.topic)) {
            return topic.replace(getTopic(), "");
        }
        else
            return null;
    }

    //Add sensor to device
    public void addSensor(Sensor sensor) {
        if(sensor != null)
            availableSensors.add(sensor);
    }
    //Remove sensor from device
    public void removeSensor(Sensor sensor) {
        if(sensor != null) {
            availableSensors.remove(sensor);
        }
    }

    //<editor-fold desc="GETTERS">
    public String getTopic() {
        return topic;
    }

    public String getName() {
        return name;
    }

    public Location getRoom() {
        return room;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Sensor[] getSensors() {
        Sensor[] sensors = new Sensor[availableSensors.size()];
        availableSensors.toArray(sensors);
        return  sensors;
    }
    //</editor-fold>

    //<editor-fold desc="SETTERS">
    public void setName(String name) {
        this.name = name;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDeviceType(DeviceType type) {
        this.deviceType = type;
    }

    public void setDataType(DataType type) {
        this.dataType = type;
    }

    public void setRoom(Location room) {
        this.room = room;
    }
    //</editor-fold>


}
