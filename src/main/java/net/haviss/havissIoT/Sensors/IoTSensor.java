package net.haviss.havissIoT.Sensors;


import net.haviss.havissIoT.Communication.MQTTQOS;
import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.Location;
import net.haviss.havissIoT.Type.SensorType;
import net.haviss.havissIoT.Type.SensorUnit;
import org.jetbrains.annotations.NotNull;
import net.haviss.havissIoT.Type.Location;

/**
 * Created by havar on 06.03.2016.
 */
public class IoTSensor {
    private String name;
    private String topic;
    private String lastValue = null;
    private Location location = null;
    private MQTTQOS qos = MQTTQOS.ATMOSTONCE;
    protected SensorType sensorType;
    protected DataType dataType;
    protected SensorUnit unit;


    public IoTSensor(@NotNull String name, @NotNull String topic, SensorType type, DataType dataType, SensorUnit unit) {
        this.name = name;
        this.topic = topic;
        this.sensorType = type;
        this.dataType = dataType;
        this.unit = unit;
    }

    public IoTSensor(@NotNull String name, @NotNull String topic, SensorType type, DataType dataType, SensorUnit unit, Location location, MQTTQOS qos) {
        this.name = name;
        this.topic = topic;
        this.location = location;
        this.sensorType = type;
        this.dataType = dataType;
        this.unit = unit;
        this.qos = qos;
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

    public Location getLocation() {
        return this.location;
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

    public DataType getDataType() {
        return dataType;
    }

    public MQTTQOS getQos() {
        return qos;
    }
}

