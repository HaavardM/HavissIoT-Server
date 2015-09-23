package net.haviss.havissIoT.Type;
import com.sun.istack.internal.NotNull;

import java.util.Date;

/**
 * Created by Håvard on 8/29/2015.
 */
public class IoTSensor {

    //Variables
    private String name;
    private String topic;
    private String type;
    private String lastValue = null;
    private Room room;

    //Constructor
    public IoTSensor(@NotNull String name, @NotNull String topic, @NotNull String type) {
        this.name = name;
        this.topic = topic;
        this.type = type;
        this.room = null;
    }

    //Overloaded constructor
    public IoTSensor(@NotNull String name, @NotNull String topic, @NotNull String type, Room room) {
        this.name = name;
        this.topic = topic;
        this.type = type;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public String getType() {
        return type;
    }

    public String getLastValue() {
        return lastValue;
    }

    public Room getRoom() {
        return room;
    }

    public void updateValue(String value) {
        lastValue = value;
    }
}
