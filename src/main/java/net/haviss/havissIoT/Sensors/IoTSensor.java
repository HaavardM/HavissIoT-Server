package net.haviss.havissIoT.Sensors;


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

    public IoTSensor(@NotNull String name, @NotNull String topic, @NotNull String type) {
        this.name = name;
        this.topic = topic;
    }

    public IoTSensor(@NotNull String name, @NotNull String topic, @NotNull String type, Room room) {
        this.name = name;
        this.topic = topic;
        this.room = room;
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
}
