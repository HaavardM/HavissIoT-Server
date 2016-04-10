package net.haviss.havissIoT.Device;

import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Tools.Topic;
import org.apache.http.HttpStatus;

/**
 * Created by H�vard on 7/13/2015.
 */
public class PhotonLight implements DeviceCallback<Boolean> {

    public Topic topic = null;
    public String name;

    public PhotonLight(String name, String room) {
        topic = new Topic(Config.houseName, room, name);
        this.name = name;
    }

    @Override
    public String set(Boolean parameters) {
        String toSend;
        toSend = parameters ? "on" : "off";
        try {
            Main.client.publishMessage(getDeviceTopic(), toSend);
        } catch (HavissIoTMQTTException e) {
            Main.printMessage(e.getMessage());
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
}
