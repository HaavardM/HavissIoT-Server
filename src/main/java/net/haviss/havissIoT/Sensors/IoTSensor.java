package net.haviss.havissIoT.Sensors;


import net.haviss.havissIoT.Type.IoTDataType;
import net.haviss.havissIoT.Type.SensorType;
import net.haviss.havissIoT.Type.SensorUnit;
import org.jetbrains.annotations.NotNull;
import net.haviss.havissIoT.Type.Room;

/**
 * Created by havar on 06.03.2016.
 */
public class IoTSensor {
    private String name;
    private String topic;
    private String lastValue = null;
    private Room room = null;
    private SensorType sensorType;
    private IoTDataType dataType;
    private SensorUnit unit;

    public IoTSensor(@NotNull String name, @NotNull String topic, SensorType type, IoTDataType dataType, SensorUnit unit) {
        this.name = name;
        this.topic = topic;
        this.sensorType = type;
        this.dataType = dataType;
        this.unit = unit;
    }

    public IoTSensor(@NotNull String name, @NotNull String topic, SensorType type, IoTDataType dataType, SensorUnit unit, Room room) {
        this.name = name;
        this.topic = topic;
        this.room = room;
        this.sensorType = type;
        this.dataType = dataType;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public String getLastValue() {
        return lastValue;
    }

    public Room getRoom() {
        return this.room;
    }

    public void updateValue(String value) {
        lastValue = value;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public SensorUnit getUnit() {
        return unit;
    }

    public IoTDataType getDataType() {
        return dataType;
    }
}

