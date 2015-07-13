package net.haviss.havissIoT.Device;

import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Tools.Topic;
import org.apache.http.HttpStatus;

/**
 * Created by Håvard on 7/13/2015.
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
        HavissIoT.client.publishMessage(getDeviceTopic(), toSend);
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
