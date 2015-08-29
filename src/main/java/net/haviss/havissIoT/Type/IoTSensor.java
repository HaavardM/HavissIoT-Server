package net.haviss.havissIoT.Type;

import com.sun.istack.internal.NotNull;

/**
 * Created by Håvard on 8/29/2015.
 */
public class IoTSensor {

    private String name;
    private String topic;
    private String type;
    private String location;

    public IoTSensor(@NotNull String name, @NotNull String topic, @NotNull String type, String location) {
        this.name = name;
        this.topic = topic;
        this.type = type;
        this.location = location;
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

    public String getLocation() {
        return location;
    }


}
