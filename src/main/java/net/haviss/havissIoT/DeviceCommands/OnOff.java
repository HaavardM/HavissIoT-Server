package net.haviss.havissIoT.DeviceCommands;

import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Tools.Topic;
import org.apache.http.HttpStatus;

/**
 * Created by Håvard on 7/13/2015.
 */
public class OnOff implements DeviceCallback<Boolean> {

    public Topic topic = null;
    public String name;

    public OnOff(String name, String room) {
        topic = new Topic(Config.houseName, room, name);
        this.name = name;
    }

    @Override
    public String set(Boolean parameters) {
        String toSend;
        toSend = parameters ? "on" : "off";
        try {
            HavissIoT.client.publishMessage(getDeviceTopic(), toSend);
        } catch (HavissIoTMQTTException e) {
            HavissIoT.printMessage(e.getMessage());
        }
        return Integer.toString(HttpStatus.SC_OK);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDeviceTopic() {
        return topic.toString();
    }

    @Override
    public String getType() {
        return "ON_OFF";
    }
}
