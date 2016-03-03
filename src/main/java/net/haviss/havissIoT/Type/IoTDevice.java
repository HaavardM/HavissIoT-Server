package net.haviss.havissIoT.Type;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by haavard on 21.02.2016.
 */
public class IoTDevice {
    private CopyOnWriteArrayList<IoTSensor> sensors = new CopyOnWriteArrayList<>();
    private String name, topic;

    public String getName() {
        return this.name;
    }

    public String getTopic() {
        return this.topic;
    }
}
