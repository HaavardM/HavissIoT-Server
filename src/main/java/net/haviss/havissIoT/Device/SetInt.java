package net.haviss.havissIoT.Device;

import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.HavissIoT;
import org.apache.http.HttpStatus;

/**
 * Created by Håvard on 7/20/2015.
 */
public class SetInt implements DeviceCallback<Integer> {

    private String name;
    private String topic;

    public SetInt(String name, String topic) {
        this.name = name;
        this.topic = topic;
    }

    @Override
    public String set(Integer parameters) {
        try {
            HavissIoT.client.publishMessage(topic, parameters.toString());
            return Integer.toString(HttpStatus.SC_OK);
        } catch (HavissIoTMQTTException e) {
            HavissIoT.printMessage(e.getMessage());
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
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
        return "SET_INT";
    }
}
