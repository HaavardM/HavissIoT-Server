package net.haviss.havissIoT.Devices;

import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.Room;

/**
 * Created by havar on 06.03.2016.
 */
public class DimmerDevice extends Device {

    private int status = 0;

    public DimmerDevice(String name, String topic, Room room) {
        super(name, topic, room);
        try {
            HavissIoT.client.subscribeToTopic(getTopic() + "/status", Config.qos);
        } catch (HavissIoTMQTTException e) {
            HavissIoT.printMessage(e.getMessage());
        }
    }

    public DimmerDevice(String name, String topic) {
        super(name, topic);
        try {
            HavissIoT.client.subscribeToTopic(getTopic() + "/status", Config.qos);
        } catch (HavissIoTMQTTException e) {
            HavissIoT.printMessage(e.getMessage());
        }
    }

    public void setState(int state) {
        if(state >= 0 && state <= 255) {
            try {
                HavissIoT.client.publishMessage(getTopic() + "/set", Integer.toString(state));
            } catch (HavissIoTMQTTException e) {
                HavissIoT.printMessage(e.getMessage());
            }
        }
    }

    public void updateStatus(int value) {
        status = value;
    }


}
