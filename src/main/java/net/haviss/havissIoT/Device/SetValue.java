package net.haviss.havissIoT.Device;

import net.haviss.havissIoT.HavissIoT;

/**
 * Created by Håvard on 7/20/2015.
 */
public class SetValue<T> implements DeviceCallback<T> {

    private String name;
    private String topic;

    public SetValue(String name, String topic) {
        this.name = name;
        this.topic = topic;
    }

    @Override
    public String set(T parameters) {
        if(T instanceof Integer)
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDeviceTopic() {
        return topic;
    }

    @Override
    public String getType() {
        return "SET_VALUE";
    }
}
