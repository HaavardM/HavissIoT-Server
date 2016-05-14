package net.haviss.havissIoT.Sensors;


import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.Location;
import net.haviss.havissIoT.Type.SensorType;
import net.haviss.havissIoT.Type.SensorUnit;
import org.jetbrains.annotations.NotNull;
import net.haviss.havissIoT.Type.Location;

/**
 * Created by havar on 06.03.2016.
 */
public class Sensor {
    private String name;
    private String topic;
    private String lastValue = null;
    private Location location = null;
    protected SensorType sensorType;
    protected DataType dataType;
    protected SensorUnit unit;

    public Sensor(@NotNull String name, @NotNull String topic, SensorType type, DataType dataType, SensorUnit unit) {
        this.name = name;
        this.topic = topic;
        this.sensorType = type;
        this.dataType = dataType;
        this.unit = unit;
    }

    public Sensor(@NotNull String name, @NotNull String topic, SensorType type, DataType dataType, SensorUnit unit, Location location) {
        this.name = name;
        this.topic = topic;
        this.location = location;
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
}

